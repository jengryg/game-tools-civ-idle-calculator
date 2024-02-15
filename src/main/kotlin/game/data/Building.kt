package game.data

import kotlin.math.pow
import kotlin.math.roundToLong

class Building(
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

    fun allInputsCalculated(): Boolean {
        return input.values.all { it.resource.tier != null && it.resource.price != null }
    }

    fun maxInputTier(): Resource {
        return input.values.maxBy { it.resource.tier!! }.resource
    }

    fun totalInputValue(): Double {
        return input.values.sumOf { it.amount * it.resource.price!! }
    }

    fun totalOutputAmount(): Long {
        return output.filter { it.key != "Science" }.values.sumOf { it.amount }
    }

    fun getCostForBuildingOneLevel(level: Int): Map<String, ResourceAmount> {
        return construction.ifEmpty { input }.mapValues { (_, base) ->
            if (special == BuildingType.WORLD_WONDER) {
                ResourceAmount(
                    resource = base.resource,
                    amount = ((costMultiplier!! * base.amount * base.resource.tier!!) / base.resource.price!!.pow(0.9)).roundToLong()
                )
            } else {
                ResourceAmount(
                    resource = base.resource,
                    amount = (costMultiplier!! * base.amount * 1.5.pow(level)).roundToLong()
                )
            }
        }
    }

    fun getCostForUpgradingLevels(currentLevel: Int, desiredLevel: Int): Map<String, ResourceAmount> {
        val result = construction.ifEmpty { input }.keys.associateWith { 0L }.toMutableMap()

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

    fun getTotalBuildingValue(currentLevel: Int): Double {
        return getCostForUpgradingLevels(0, currentLevel).values.sumOf { it.value() }
    }
}