package data.definitions.model

import com.fasterxml.jackson.annotation.JsonIgnore
import common.BuildingType
import common.ResourceAmount
import kotlin.math.pow
import kotlin.math.roundToLong

open class Building(
    val name: String,
    val input: Map<String, ResourceAmount>,
    val output: Map<String, ResourceAmount>,
    val construction: Map<String, ResourceAmount>,
    val deposit: Map<String, Deposit>,
    val special: BuildingType = BuildingType.NORMAL
) {
    var technology: Technology? = null
    var tier: Int? = null
    var costMultiplier: Double? = null

    @JsonIgnore
    fun areAllInputsCalculated(): Boolean {
        return input.values.all { it.resource.tier != null && it.resource.price != null }
    }

    @JsonIgnore
    fun getMaxInputTier(): Resource {
        return input.values.maxBy { it.resource.tier!! }.resource
    }

    fun getTotalValueOfInputs(): Double {
        return input.values.sumOf { it.amount * it.resource.price!! }
    }

    @JsonIgnore
    fun getTotalAmountOfOutputs(): Long {
        return output.filter { it.key != "Science" }.values.sumOf { it.amount }
    }

    @JsonIgnore
    fun getCostForBuildingOneLevel(level: Int): Map<String, ResourceAmount> {
        return construction.ifEmpty { input }.mapValues { (_, base) ->
            if (special == BuildingType.WORLD_WONDER) {
                ResourceAmount(
                    resource = base.resource,
                    amount = ((costMultiplier!! * base.amount) / (base.resource.price ?: 1.0)).roundToLong()
                )
            } else {
                ResourceAmount(
                    resource = base.resource,
                    amount = (1.5.pow(level) * costMultiplier!! * base.amount).roundToLong(),
                )
            }
        }
    }

    @JsonIgnore
    fun getCostForUpgradingLevelsFromTo(currentLevel: Int, desiredLevel: Int): Map<String, ResourceAmount> {
        val result = construction.ifEmpty { input }.keys.associateWith { 0L }.toMutableMap()

        if (special == BuildingType.NATURAL_WONDER || special == BuildingType.HQ) {
            return mapOf()
        }

        for (i in currentLevel until desiredLevel) {
            getCostForBuildingOneLevel(i).forEach { (rName, cost) ->
                result[rName] = result[rName]!! + cost.amount
            }
        }

        return construction.ifEmpty { input }.map { (rName, cost) ->
            rName to ResourceAmount(
                resource = cost.resource,
                amount = result[rName]!!
            )
        }.toMap()
    }

    @JsonIgnore
    fun getTotalBuildingValueAtLevel(currentLevel: Int): Double {
        return getCostForUpgradingLevelsFromTo(0, currentLevel).values.sumOf {
            if (it.resource.name in listOf("Worker", "Power", "Science", "Warp")) {
                0.0
            } else {
                it.enterpriseValue()
            }
        }
    }
}