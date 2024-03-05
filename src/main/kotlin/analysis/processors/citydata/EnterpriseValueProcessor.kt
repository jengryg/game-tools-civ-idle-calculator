package analysis.processors.citydata

import Logging
import OUTPUT_PATH
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import common.BuildingType
import common.ResourceAmount
import logger
import utils.FileIo
import utils.nf
import kotlin.math.floor

class EnterpriseValueProcessor(
    private val ap: AnalyserProvider
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    val tiles = psa.tiles.values.toList()
    val buildings = tiles.mapNotNull { it.building }.filter { it.building.special == BuildingType.NORMAL }
    val wonders = tiles.mapNotNull { it.building }.filter { it.building.special == BuildingType.WORLD_WONDER }

    val transportation = psa.transportations.values.toList()

    fun createReport() {
        val buildingValuesByTier = buildings.groupBy { it.building.tier!! }.mapValues { (_, bList) ->
            bList.sumOf { it.investedEnterpriseValue.values.sum() }
        }

        val buildingValuesByName = buildings.groupBy { it.building.name }.mapValues { (_, bList) ->
            bList.sumOf { it.investedEnterpriseValue.values.sum() }
        }

        val buildingValueTotal = buildingValuesByTier.values.sum()

        val wonderValuesByName = wonders.groupBy { it.building.name }
            .mapValues { it.value.sumOf { b -> b.investedEnterpriseValue.values.sum() } }

        val wonderValueTotal = wonderValuesByName.values.sum()

        val investedValueTotal = buildingValueTotal + wonderValueTotal

        val storedResourceAmount = tiles.mapNotNull { it.building }.flatMap {
            it.resources
        }.groupBy { it.resource }.map {
            ResourceAmount(
                resource = it.key,
                amount = it.value.sumOf { ra -> ra.amount }
            )
        }

        val storedValuesByTier = storedResourceAmount.groupBy { it.resource.tier }.mapValues {
            it.value.sumOf { ra -> ra.enterpriseValue() }
        }

        val storedValueTotal = storedResourceAmount.sumOf {
            it.enterpriseValue()
        }

        val transportationResourceAmount = transportation.map { it.resourceAmount }.groupBy {
            it.resource
        }.map {
            ResourceAmount(
                resource = it.key,
                amount = it.value.sumOf { ra -> ra.amount }
            )
        }

        val transportationValuesByTier = transportationResourceAmount.groupBy { it.resource.tier }.mapValues {
            it.value.sumOf { ra -> ra.enterpriseValue() }
        }

        val transportationTotal = transportationResourceAmount.sumOf {
            it.enterpriseValue()
        }

        val totalEmpireValue = storedValueTotal + investedValueTotal + transportationTotal

        val extraGP = floor(gda.getExtraGP(totalEmpireValue))
        val nextExtraGP = gda.getEnterpriseValueForGP(extraGP + 1)

        val reportContent = mutableListOf(
            listOf(
                "Total Normal Building Value" to buildingValueTotal.nf(),
                "Total Wonder Building Value" to wonderValueTotal.nf(),
                "Total Stored Value" to storedValueTotal.nf(),
                "Total Transported Value" to transportationTotal.nf(),
                "Total Empire Value" to totalEmpireValue.nf(),
                "Next Bonus GP at" to nextExtraGP.nf(),
                "Current Bonus GP" to extraGP.nf(),
            )
        ).joinToString("\n") {
            it.joinToString("\n") { (k, v) ->
                "${k.padEnd(60)}${v.padStart(30)}"
            }
        }

        val buildingsReportContent = mutableListOf(
            buildingValuesByTier.map {
                "Invested Value Normal Tier ${it.key}" to it.value.nf()
            }.sortedBy { it.first },
            null,
            buildingValuesByName.map {
                "Invested Value Normal Name ${it.key}" to it.value.nf()
            }.sortedBy { it.first },
            null,
            wonderValuesByName.map {
                "Invested Value Wonder Name ${it.key}" to it.value.nf()
            }
        ).joinToString("\n") {
            it?.joinToString("\n") { (k, v) ->
                "${k.padEnd(60)}${v.padStart(30)}"
            } ?: "=".repeat(90)
        }

        val resourcesReportContent = mutableListOf(
            storedResourceAmount.map {
                listOf("Stored Amount and Value ${it.resource.name}", it.amount.nf(), it.enterpriseValue().nf())
            }.sortedBy { it[0] },
            null,
            storedValuesByTier.map {
                listOf("Stored Value by Resource Tier ${it.key}", "", it.value.nf())
            }.sortedBy { it[0] }
        ).joinToString("\n") {
            it?.joinToString("\n") { row ->
                "${row[0].padEnd(60)}${row[1].padStart(30)}${row[2].padStart(30)}"
            } ?: "=".repeat(120)
        }

        val transportationReportContent = mutableListOf(
            transportationResourceAmount.map {
                listOf("Transported Amount and Value ${it.resource.name}", it.amount.nf(), it.enterpriseValue().nf())
            }.sortedBy { it[0] },
            null,
            transportationValuesByTier.map {
                listOf("Transported Value by Resource Tier ${it.key}", "", it.value.nf())
            }
        ).joinToString("\n") {
            it?.joinToString("\n") { row ->
                "${row[0].padEnd(60)}${row[1].padStart(30)}${row[2].padStart(30)}"
            } ?: "=".repeat(120)
        }

        FileIo.writeFile(
            "$OUTPUT_PATH/enterprise-report.txt",
            listOf(reportContent, buildingsReportContent, resourcesReportContent).joinToString("\n\n\n")
        )
    }
}