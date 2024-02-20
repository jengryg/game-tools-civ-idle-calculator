package analysis

import common.BuildingType
import common.ResourceAmount
import data.definitions.GameDefinition
import data.player.PlayerState
import data.player.model.BuildingStatus
import utils.nf

class Analyzer(
    private val gameDefinitions: GameDefinition,
    private val playerState: PlayerState
) {
    val tiles = playerState.tiles.values.toList()

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

    private fun calculateImpactOfGrotto(): Double {
        return tiles.sumOf { tile ->
            tile.building.takeIf { it?.building?.tier == 1 }?.let {
                it.building.getCostForUpgradingLevelsFromTo(it.level, it.level + 5).values.sumOf { ra ->
                    ra.enterpriseValue()
                }
            } ?: 0.0
        }
    }

    fun analyze(): Map<String, Any?> {
        val totalBuildingsValues = totalBuildings().flatMap { outer ->
            val bld = gameDefinitions.buildings[outer.key]!!
            outer.value.mapIndexed { index, level ->
                "${bld.name} #$index" to bld.getTotalBuildingValueAtLevel(level)
            }
        }.toMap()
        val buildingsSum = totalBuildingsValues.values.sumOf { it }

        val totalResourcesValues = totalResources().mapValues {
            it.value.enterpriseValue()
        }.filterValues { it > 0 }.entries.sortedBy { it.value }.associate { it.key to it.value }

        val resourcesSum = totalResourcesValues.values.sumOf { it }

        val totalEmpireValue = buildingsSum + resourcesSum

        val totalGrotto = calculateImpactOfGrotto()

        return mapOf(
            "1.  Building Values" to totalBuildingsValues,
            "2.  Resource Values" to totalResourcesValues,
            "3.1 Total Building Value" to buildingsSum.nf(),
            "3.2 Total Resource Values" to resourcesSum.nf(),
            "3.3 Total Empire Value" to totalEmpireValue.nf(),
            "4   Grotto Impact Value" to totalGrotto.nf(),
            "4.1 Total Value Grotto" to (totalGrotto + resourcesSum + buildingsSum).nf(),
        )
    }

    private fun totalResources(): Map<String, ResourceAmount> {
        val initial = gameDefinitions.resources.mapValues { ResourceAmount(resource = it.value) }

        tiles.forEach { tile ->
            tile.building?.also {
                if (it.status != BuildingStatus.BUILDING) {
                    it.resources.forEach { ra ->
                        initial[ra.resource.name]!!.amount += ra.amount
                    }
                }
            }
        }

        return initial
    }

    private fun totalBuildings(): Map<String, List<Int>> {
        val initial = gameDefinitions.buildings.mapValues { mutableListOf<Int>() }

        tiles.forEach { tile ->
            tile.building?.also {
                if (it.status != BuildingStatus.BUILDING && it.building.special != BuildingType.NATURAL_WONDER && it.building.special != BuildingType.HQ) {
                    initial[it.building.name]!!.add(it.level)
                }
            }
        }

        return initial
    }
}