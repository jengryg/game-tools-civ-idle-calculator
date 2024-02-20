package analysis.processors.citydata

import Logging
import OUTPUT_PATH
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import common.BuildingType
import common.ResourceAmount
import data.player.model.BuildingStatus
import logger
import utils.FileIo
import utils.nf

class EnterpriseValueProcessor(
    private val ap: AnalyserProvider
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    val tiles = psa.tiles.values.toList()

    fun createReport() {
        val totalBuildingsValues = totalBuildings().flatMap { outer ->
            val bld = gda.buildings[outer.key]!!
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

        val grottoEmpireValue = totalGrotto + totalEmpireValue

        val reportContent =
            """
            Total Building Value:      ${buildingsSum.nf().padStart(28, ' ')}
            Total Resource Value:      ${resourcesSum.nf().padStart(28, ' ')}
            Total Empire Value:        ${totalEmpireValue.nf().padStart(28, ' ')}
            ===========================${"=".repeat(28)}
            Grotto Discovery Impact:   ${totalGrotto.nf().padStart(28, ' ')}
            Grotto Empire Value:       ${grottoEmpireValue.nf().padStart(28, ' ')}
            
        """.trimIndent()

        FileIo.writeFile(
            "$OUTPUT_PATH/enterprise-report.txt",
            reportContent
        )
    }

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

    private fun totalResources(): Map<String, ResourceAmount> {
        val initial = gda.resources.mapValues { ResourceAmount(resource = it.value) }

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
        val initial = gda.buildings.mapValues { mutableListOf<Int>() }

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