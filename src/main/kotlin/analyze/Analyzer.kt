package analyze

import custom.CustomObjectRegistry
import custom.data.BuildingStatus
import game.GameObjectRegistry
import game.data.Building
import game.data.BuildingType
import utils.nf

class Analyzer(
    private val gameObjectRegistry: GameObjectRegistry,
    private val customObjectRegistry: CustomObjectRegistry
) {
    val tiles = customObjectRegistry.tiles.values.toList()

    private fun calculateCurrentBuildingValue(): Double {
        return tiles.sumOf { tile ->
            tile.building?.let {
                when (it.building.special) {
                    BuildingType.WORLD_WONDER, BuildingType.NORMAL -> it.totalCost
                    else -> 0.0
                }
            } ?: 0.0
        }
    }

    private fun calculateCurrentResourceValue(): Double {
        return tiles.sumOf { tile ->
            tile.building?.resources?.sumOf { ra -> ra.enterpriseValue() } ?: 0.0
        }
    }

    private fun calculateImpactOfGrotto(): Double {
        return tiles.sumOf { tile ->
            tile.building.takeIf { it?.building?.tier == 1 }?.let {
                it.building.getCostForUpgradingLevels(it.level, it.level + 5).values.sumOf { ra ->
                    ra.enterpriseValue()
                }
            } ?: 0.0
        }
    }

    fun analyze(): Map<String, String> {
        val totalBuildingValue = calculateCurrentBuildingValue()
        val totalResourceValue = calculateCurrentResourceValue()
        val totalGrotto = calculateImpactOfGrotto()

        return mapOf(
            "Total Building Value" to nf(totalBuildingValue),
            "Total Resource Value" to nf(totalResourceValue),
            "Total Empire Value" to nf(totalResourceValue + totalBuildingValue),
            "Grotto Impact Value" to nf(totalGrotto),
            "Total Value Grotto" to nf(totalGrotto + totalResourceValue + totalBuildingValue)
        )
    }

    fun totalResources(): MutableMap<String, Long> {
        val initial = gameObjectRegistry.resources.keys.associateWith { 0L }.toMutableMap()

        tiles.forEach { tile ->
            tile.building?.also {
                if (it.status != BuildingStatus.BUILDING) {
                    it.resources.forEach { ra ->
                        initial[ra.resource.name] = initial[ra.resource.name]!! + ra.amount
                    }
                }
            }
        }

        return initial
    }
}