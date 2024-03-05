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
import kotlin.math.floor

class EnterpriseValueProcessorLegacy(
    private val ap: AnalyserProvider
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    val tiles = psa.tiles.values.toList()

    fun createReport() {


        val totalBuildings = totalBuildings()

        val totalBuildingsValues = mutableMapOf<String, Double>()
        val tier1BuildingsValues = mutableMapOf<String, Double>()

        totalBuildings.forEach { outer ->
            val bld = gda.buildings[outer.key]!!
            outer.value.mapIndexed { index, level ->
                val ev = bld.getTotalBuildingValueAtLevel(level)
                val key = "${bld.name} #$index"

                if (bld.tier == 1 && bld.special == BuildingType.NORMAL) {
                    tier1BuildingsValues[key] = ev
                }

                totalBuildingsValues[key] = ev
            }
        }

        val buildingsSum = totalBuildingsValues.values.sumOf { it }

        val tier1BuildingSum = tier1BuildingsValues.values.sumOf { it }

        val totalResourcesValues = totalResources().mapValues {
            it.value.enterpriseValue()
        }.filterValues { it > 0 }.entries.sortedBy { it.value }.associate { it.key to it.value }

        val resourcesSum = totalResourcesValues.values.sumOf { it }

        val totalEmpireValue = buildingsSum + resourcesSum

        val totalGrotto = calculateImpactOfGrotto()

        val grottoEmpireValue = totalGrotto + totalEmpireValue

        val extraGP = floor(gda.getExtraGP(totalEmpireValue))
        val grottoGP = floor(gda.getExtraGP(grottoEmpireValue))

        val nextExtraGP = gda.getEnterpriseValueForGP(extraGP + 1)
        val nextGrottoGP = gda.getEnterpriseValueForGP(grottoGP + 1)

        val reportContent =
            """
            ${psa.city.name}
            ===========================${"=".repeat(28)}
            Total Building Value:      ${buildingsSum.nf().padStart(28, ' ')}
            Total Resource Value:      ${resourcesSum.nf().padStart(28, ' ')}
            Total Empire Value:        ${totalEmpireValue.nf().padStart(28, ' ')}
            Extra GP:                  ${extraGP.nf().padStart(28, ' ')}
            Next GP at:                ${nextExtraGP.nf().padStart(28, ' ')}
            ===========================${"=".repeat(28)}
            Tier 1 Building Value:     ${tier1BuildingSum.nf().padStart(28, ' ')}
            Grotto Discovery Impact:   ${totalGrotto.nf().padStart(28, ' ')}
            Grotto Empire Value:       ${grottoEmpireValue.nf().padStart(28, ' ')}
            Extra GP:                  ${grottoGP.nf().padStart(28, ' ')}
            Next GP at:                ${nextGrottoGP.nf().padStart(28, ' ')}
            ===========================${"=".repeat(28)}
        """.trimIndent()

        FileIo.writeFile(
            "$OUTPUT_PATH/enterprise-report-legacy.txt",
            reportContent
        )
    }

    private fun calculateImpactOfGrotto(): Double {
        return tiles.sumOf { tile ->
            tile.building.takeIf { it?.building?.tier == 1 && it.building.special == BuildingType.NORMAL }?.let {
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
                if (it.building.special != BuildingType.NATURAL_WONDER && it.building.special != BuildingType.HQ) {
                    initial[it.building.name]!!.add(it.level)
                }
            }
        }

        return initial
    }
}