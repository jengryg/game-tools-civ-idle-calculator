package data.definitions.extension

import Logging
import common.BuildingType
import data.definitions.GameDefinition
import data.definitions.model.Building
import data.definitions.model.Resource
import logger
import utils.roundHalfUp
import kotlin.math.pow
import kotlin.math.round

object GameDataCalculator : Logging {
    private val log = logger()

    fun calculateTierAndPrice(gd: GameDefinition) {
        processDeposits(gd)
        val chain = processSimpleBuildings(gd)
        processChainCalculation(gd, chain)
        finalizeResources(gd)
        calculateBuildingCostMultiplier(gd)
    }

    private fun processDeposits(gor: GameDefinition) {
        gor.deposits.forEach { (dName, deposit) ->
            gor.resources[dName]!!.apply {
                tier = 1
                price = round(
                    deposit.technology!!.column.toDouble() + (deposit.technology!!.age.id.toDouble()).pow(2)
                )

                log.atDebug()
                    .setMessage("Calculated Resource Tier and Price from Deposit.")
                    .addKeyValue("name") { name }
                    .addKeyValue("tier") { tier }
                    .addKeyValue("price") { price }
                    .log()
            }
        }
    }

    private fun processSimpleBuildings(gor: GameDefinition): List<Building> {
        val chainCalculation = mutableListOf<Building>()

        gor.buildings.forEach { (bName, building) ->
            if (building.input.isEmpty()) {
                building.output.forEach { (_, ra) ->
                    ra.resource.apply {
                        if (tier == null) tier = 1
                        if (price == null || price == 0.0) {
                            price = 1.0 + (building.technology?.column ?: 0).toDouble()

                            log.atDebug()
                                .setMessage("Calculated Resource Price from Factory with no inputs.")
                                .addKeyValue("name") { name }
                                .addKeyValue("tier") { tier }
                                .addKeyValue("price") { price }
                                .log()
                        }
                    }
                }
                building.apply {
                    if (tier == null) tier = 1

                    log.atDebug()
                        .setMessage("Calculated Building Tier.")
                        .addKeyValue("name") { name }
                        .addKeyValue("tier") { tier }
                        .log()
                }
            }

            if (building.input.isNotEmpty() && building.output.isNotEmpty()) {
                chainCalculation.add(building)
            }

            assert(building.technology != null || !building.special.requiresUnlockTech) {
                "Building $bName is not unlocked by any tech!"
            }

            building.input.forEach { (rName, ra) ->
                ra.resource.technology!!.let {
                    assert(it.column < building.technology!!.column) {
                        """
                            Building $bName unlocks with ${building.technology!!.name} ${building.technology!!.column} 
                            Resource $rName unlocks with ${it.name} ${it.column}
                            Resource must unlock before building can unlock.
                        """.trimIndent()
                    }
                }
            }
        }

        return chainCalculation.toList()
    }

    private val resourceTierDependency = mutableMapOf<Resource, Resource>()
    private val buildingTierDependency = mutableMapOf<Building, Resource>()

    private fun processChainCalculation(gor: GameDefinition, chain: List<Building>) {
        var iteration = 0
        while (iteration <= 10) {
            val nullTiers = gor.buildings.values.count { it.tier == null }

            if (nullTiers != 0) {
                log.atInfo()
                    .setMessage("There are still buildings without known tier left.")
                    .addKeyValue("iteration") { iteration }
                    .addKeyValue("remaining#") { nullTiers }
                    .addKeyValue("items") {
                        gor.buildings.values.filter { it.tier == null }.joinToString(" ", "[", "]") { it.name }
                    }
                    .addKeyValue("chain#") { chain.size }
                    .log()
            } else {
                log.atInfo()
                    .setMessage("Chain Calculation finished.")
                    .addKeyValue("iteration") { iteration }
                    .addKeyValue("remaining") { 0 }
                    .addKeyValue("chain#") { chain.size }
                    .log()
                break
            }

            resourceTierDependency.clear()
            buildingTierDependency.clear()

            chain.filter { it.areAllInputsCalculated() }.forEach { building ->
                log.atTrace()
                    .setMessage("Checking Update for Building.")
                    .addKeyValue("name") { building.name }
                    .log()

                val highestInput = building.getMaxInputTier().also {
                    log.atDebug()
                        .setMessage("Highest Input Resource of building determined.")
                        .addKeyValue("building") { building.name }
                        .addKeyValue("buildingTier") { building.tier }
                        .addKeyValue("resource") { it.name }
                        .addKeyValue("resourceTier") { it.tier }
                        .addKeyValue("resourcePrice") { it.price }
                        .log()
                }

                val targetTier = 1 + highestInput.tier!!
                var internallyPricedResourceValues = 0.0

                building.output.forEach { (rName, output) ->
                    if (rName != "Science") {
                        if (output.resource.tier == null || targetTier < output.resource.tier!!) {
                            val oldTier = output.resource.tier
                            output.resource.tier = targetTier
                            updateDependencies(output.resource, highestInput, targetTier, oldTier)
                        }
                    } else {
                        internallyPricedResourceValues += output.amount.toDouble() * 0.5
                        log.atDebug()
                            .setMessage("Building produces science. Internal valuation at 0.5.")
                            .addKeyValue("amount") { output.amount }
                            .addKeyValue("value") { output.amount * 0.5 }
                            .log()
                    }
                }

                if (building.tier == null || targetTier > building.tier!!) {
                    log.atTrace()
                        .setMessage("Update Building Tier")
                        .addKeyValue("name") { building.name }
                        .addKeyValue("current") { building.tier }
                        .addKeyValue("target") { targetTier }
                        .log()

                    building.tier = targetTier
                } else {
                    log.atTrace()
                        .setMessage("Building is already at a higher or equal tier.")
                        .addKeyValue("name") { building.name }
                        .addKeyValue("current") { building.tier }
                        .addKeyValue("target") { targetTier }
                        .log()
                }
                val multiplier = 1.5 + 0.25 * building.input.size
                building.output.values.filter { it.resource.canPrice }.forEach {
                    val price =
                        ((multiplier * building.getTotalValueOfInputs() - internallyPricedResourceValues) / building.getTotalAmountOfOutputs()).roundHalfUp()
                    if (it.resource.price == null || price > it.resource.price!!) {
                        log.atTrace()
                            .setMessage("Update Resource Price")
                            .addKeyValue("name") { it.resource.name }
                            .addKeyValue("current") { it.resource.price }
                            .addKeyValue("target") { price }
                            .log()

                        it.resource.price = price
                    } else {
                        log.atTrace()
                            .setMessage("Resource is already at a higher or equal price.")
                            .addKeyValue("name") { it.resource.name }
                            .addKeyValue("current") { it.resource.price }
                            .addKeyValue("target") { price }
                            .log()
                    }
                }
            }

            iteration++
        }
    }

    private fun updateDependencies(resource: Resource, highestInput: Resource, targetTier: Int, oldTier: Int?) {
        resourceTierDependency[resource] = highestInput

        log.atTrace()
            .setMessage("Setting Tier of Resource.")
            .addKeyValue("resource") { resource.name }
            .addKeyValue("tier") { targetTier }
            .log()

        resourceTierDependency.forEach {
            if (it.value == resource) {
                it.key.tier = null
                log.atDebug()
                    .setMessage("Resource Tier needs recalculation.")
                    .addKeyValue("update") { it.key.name }
                    .addKeyValue("cause") { resource.name }
                    .addKeyValue("causeTierBefore") { oldTier }
                    .addKeyValue("causeTierNow") { resource.tier }
                    .log()
            }
        }

        resourceTierDependency.filter { it.value == resource }.keys.forEach {
            resourceTierDependency.remove(it)
        }

        buildingTierDependency.forEach {
            if (it.value == resource) {
                it.key.tier = null
                log.atDebug()
                    .setMessage("Building Tier needs recalculation.")
                    .addKeyValue("update") { it.key.name }
                    .addKeyValue("causeTierBefore") { oldTier }
                    .addKeyValue("causeTierNow") { resource.tier }
                    .log()
            }
        }

        buildingTierDependency.filter { it.value == resource }.keys.forEach {
            buildingTierDependency.remove(it)
        }
    }

    private fun finalizeResources(gor: GameDefinition) {
        gor.resources.forEach { (name, resource) ->
            if (resource.canPrice) {
                assert(resource.price != null) { "$name can be priced but price is null!" }
                assert(resource.tier != null) { "$name can be priced but tier is null!" }
            } else {
                resource.tier = 1
                resource.price = 1.0
            }
        }
    }

    private fun calculateBuildingCostMultiplier(gor: GameDefinition) {
        gor.buildings.forEach { (name, building) ->
            building.costMultiplier = if (building.special == BuildingType.WORLD_WONDER) {
                val techIdx = building.technology?.column ?: 0
                val ageIdx = building.technology?.age?.id ?: 0

                round(
                    300 +
                            10 * ageIdx.toDouble().pow(3) * techIdx.toDouble().pow(2) +
                            (100 * 5.0.pow(ageIdx) * 1.5.pow(techIdx)) / techIdx.toDouble().pow(2)
                ).also {
                    log.atDebug()
                        .setMessage("Calculated build cost multiplier for World Wonder")
                        .addKeyValue("name") { name }
                        .addKeyValue("multiplier") { it }
                        .log()
                }
            } else {
                10.0.also {
                    log.atDebug()
                        .setMessage("Calculated build cost multiplier for normal Building.r")
                        .addKeyValue("name") { name }
                        .addKeyValue("multiplier") { it }
                        .log()
                }
            }
        }
    }
}