package game.loader.game.tasks

import Logging
import constants.ENTERPRISE_VALUE_OF_SCIENCE
import game.loader.game.GameData
import game.loader.game.IGameData
import game.loader.game.data.BuildingData
import game.loader.game.data.ResourceData
import game.loader.game.data.TechnologyData
import logger
import java.math.RoundingMode
import kotlin.math.pow

/**
 * Mimics the pricing and tier calculation algorithm used in the game to calculate the prices and tiers from the
 * production chains as they are defined by the inputs and outputs of the buildings.
 */
class ResourceTierTask(
    private val gd: GameData
) : IGameData by gd, Logging {
    private val log = logger()

    fun process() {
        processDepositResources()
        processSourceBuildings()
        processTransmutationBuildings()
        finalizeResources()
    }

    private fun processDepositResources() {
        resources.values.forEach {
            if (it.deposit != null) {
                it.apply {
                    tier = 1
                    price = getPriceFromDepositTechnology(it.deposit.revealedBy)

                    log.atTrace()
                        .setMessage("Calculated Resource Tier and Price from Deposit.")
                        .addKeyValue("name") { name }
                        .addKeyValue("tier") { tier }
                        .addKeyValue("price") { price }
                        .log()
                }
            }
        }
    }

    private fun getPriceFromDepositTechnology(technologyData: TechnologyData): Double {
        return technologyData.column.toDouble() + technologyData.age.id.toDouble().pow(2)
    }

    private fun processSourceBuildings() {
        buildings.values.filter { it.input.isEmpty() }.forEach { bld ->
            bld.output.forEach { (r, _) ->
                r.apply {
                    if (tier == null) tier = 1
                    if (price == null || price == 0.0) {
                        price = bld.unlockedBy?.let { getPriceFromBuildingTechnology(it) }

                        log.atTrace()
                            .setMessage("Calculated Resource Price from Factory with no inputs.")
                            .addKeyValue("name") { name }
                            .addKeyValue("tier") { tier }
                            .addKeyValue("price") { price }
                            .addKeyValue("building") { bld.name }
                            .log()
                    }
                }
            }

            if (bld.tier == null) {
                bld.tier = 1

                log.atTrace()
                    .setMessage("Calculated Building Tier.")
                    .addKeyValue("name") { bld.name }
                    .addKeyValue("tier") { bld.tier }
                    .log()
            }
        }
    }

    private fun getPriceFromBuildingTechnology(technologyData: TechnologyData): Double {
        return 1.0 + technologyData.column.toDouble()
    }

    private fun processTransmutationBuildings() {
        val chain = buildings.values.filter { it.input.isNotEmpty() && it.output.isNotEmpty() }.toMutableList()

        var iteration = 0
        while (iteration <= buildings.size && buildings.values.any { it.tier != null }) {
            iteration++

            val resourceTierDependency = mutableMapOf<ResourceData, ResourceData>()

            chain.filter { it.input.keys.all { r -> r.tier != null && r.price != null } }.forEach { bld ->
                log.atTrace()
                    .setMessage("Checking Update for Building.")
                    .addKeyValue("name") { bld.name }
                    .log()

                val highestTierInput = bld.input.keys.maxBy { it.tier!! }.also {
                    log.atTrace()
                        .setMessage("Highest Input Resource of building determined.")
                        .addKeyValue("building") { bld.name }
                        .addKeyValue("buildingTier") { bld.tier }
                        .addKeyValue("resource") { it.name }
                        .addKeyValue("resourceTier") { it.tier }
                        .addKeyValue("resourcePrice") { it.price }
                        .log()
                }

                val targetTier = 1 + highestTierInput.tier!!

                bld.output.filterKeys { !it.isScience }.forEach { (r, _) ->
                    if (r.tier == null || targetTier < r.tier!!) {
                        val oldTier = r.tier
                        r.tier = targetTier
                        // dependency update
                        resourceTierDependency[r] = highestTierInput

                        log.atTrace()
                            .setMessage("Setting Tier of Resource.")
                            .addKeyValue("resource") { r.name }
                            .addKeyValue("tier") { targetTier }
                            .log()

                        // reset all resources that indicate that they are dependent on the current resource
                        resourceTierDependency.filterValues { r == it }.keys.forEach {
                            it.tier = null
                            log.atTrace()
                                .setMessage("Resource Tier needs recalculation.")
                                .addKeyValue("update") { it.name }
                                .addKeyValue("cause") { r.name }
                                .addKeyValue("causeTierBefore") { oldTier }
                                .addKeyValue("causeTierNow") { r.tier }
                                .log()
                        }
                        resourceTierDependency.filterValues { r == it }.keys.forEach { resourceTierDependency.remove(it) }
                    }
                }

                if (bld.tier == null || targetTier > bld.tier!!) {
                    log.atTrace()
                        .setMessage("Update Building Tier")
                        .addKeyValue("name") { bld.name }
                        .addKeyValue("current") { bld.tier }
                        .addKeyValue("target") { targetTier }
                        .log()
                    bld.tier = targetTier
                } else {
                    log.atTrace()
                        .setMessage("Building is already at a higher or equal tier.")
                        .addKeyValue("name") { bld.name }
                        .addKeyValue("current") { bld.tier }
                        .addKeyValue("target") { targetTier }
                        .log()
                }

                val price = getPriceFromInputsAndOutputs(bld)

                bld.output.filterKeys { it.canPrice }.forEach { (r, a) ->
                    if (r.price == null || price > r.price!!) {
                        log.atTrace()
                            .setMessage("Update Resource Price")
                            .addKeyValue("name") { r.name }
                            .addKeyValue("current") { r.price }
                            .addKeyValue("target") { price }
                            .log()

                        r.price = price
                    } else {
                        log.atTrace()
                            .setMessage("Resource is already at a higher or equal price.")
                            .addKeyValue("name") { r.name }
                            .addKeyValue("current") { r.price }
                            .addKeyValue("target") { price }
                            .log()
                    }
                }
            }
        }
    }

    private fun getPriceFromInputsAndOutputs(bld: BuildingData): Double {
        val inputsValue = bld.input.filterKeys { it.canPrice }.map { (r, a) -> r.price!! * a }.sum()
        val inputTypes = bld.input.size

        val internallyPricedResourceValue =
            bld.output.filterKeys { it.isScience }.values.sum() * ENTERPRISE_VALUE_OF_SCIENCE

        val multiplier = 1.5 + 0.25 * inputTypes

        val outputValue = multiplier * inputsValue - internallyPricedResourceValue

        val outputAmount = bld.output.filterKeys { it.canPrice }.takeIf { it.isNotEmpty() }?.values?.sum()
        if (outputAmount == null || outputAmount == 0.0) {
            log.atTrace()
                .setMessage("Using price 0.0 internally since building has no valuable outputs.")
                .addKeyValue("building") { bld.name }
            return 0.0
        }

        val price = (outputValue / outputAmount).toBigDecimal().setScale(0, RoundingMode.HALF_UP).toDouble()

        log.atTrace()
            .setMessage("Calculated price for products from building IO.")
            .addKeyValue("building") { bld.name }
            .addKeyValue("inputValue") { inputsValue }
            .addKeyValue("inputTypes") { inputTypes }
            .addKeyValue("outputAmount") { outputAmount }
            .addKeyValue("reductionInternal") { internallyPricedResourceValue }
            .addKeyValue("multiplier") { multiplier }
            .addKeyValue("outputValue") { outputValue }
            .addKeyValue("pricePerUnit") { price }
            .log()

        return price
    }

    private fun finalizeResources() {
        resources.forEach { (name, r) ->
            if (r.canPrice) {
                assert(r.price != null) { "$name can be priced but price is null!" }
                assert(r.tier != null) { "$name can be priced but tier is null!" }
            } else {
                r.tier = 1
                r.price = 1.0
            }
        }
    }
}