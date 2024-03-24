package analysis.processors.general

import DATA_TEXT_INDENT
import Logging
import OUTPUT_PATH
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import logger
import utils.FileIo
import utils.nf

class BasicInformationProcessor(
    private val ap: AnalyserProvider
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    fun exportResourceList() {
        val result = ap.gda.resources.filter { it.value.canPrice }.mapNotNull { (_, r) ->
            if (r.tier == null || r.price == null) {
                null
            } else {
                Triple(
                    r.name,
                    r.tier!!,
                    r.price!!
                )
            }
        }.sortedBy { it.third }.joinToString("\n") { (name, tier, price) ->
            "${name.padEnd(20)}${tier.nf().padStart(10)}${price.nf().padStart(10)}"
        }

        FileIo.writeFile("$OUTPUT_PATH/resources.txt", result)
    }

    fun exportBuildingResourceNeeds() {
        val result = ap.gda.buildings.mapNotNull { (name, b) ->
            if (b.special.isHQ || b.special.isAnyWonder) {
                null
            } else {
                name to b.getCostForUpgradingLevelsFromTo(0, 1).values
            }

        }.sortedBy { it.first }.toMap().map { (name, cost) ->
            "$name;${cost.joinToString(";") { "${it.amount};${it.resource.name}" }}"
        }.joinToString("\n")

        FileIo.writeFile("$OUTPUT_PATH/buildings-normal-cost.txt", result)
    }

    fun exportWonderResourceNeeds() {
        val result = ap.gda.buildings.mapNotNull { (name, b) ->
            if (b.special.isWorldWonder) {
                name to b.getCostForUpgradingLevelsFromTo(0, 1).values
            } else {
                null
            }
        }.sortedBy { it.first }.toMap().map { (name, cost) ->
            "$name;${
                cost.sumOf { it.enterpriseValue() }.nf()
            };${cost.joinToString(";") { "${it.amount};${it.resource.name}" }}"
        }.joinToString("\n")

        FileIo.writeFile("$OUTPUT_PATH/buildings-wonder-cost.txt", result)
    }

    fun exportTierBasedEnterpriseValueData() {
        val output = mutableListOf<String>()

        val maxPaddingName = ap.gda.tierBasedEv.maxOf {
            it.value.maxOf { data ->
                data.key.length
            }
        }

        val maxPaddingValue = ap.gda.tierBasedEv.maxOf {
            it.value.maxOf { data ->
                data.value.nf().length
            }
        }

        ap.gda.tierBasedEv.forEach { (tier, buildings) ->
            output.add("$tier TIER:")

            buildings.forEach { (name, value) ->
                output.add(
                    " ".repeat(DATA_TEXT_INDENT)
                        .plus("${name.padEnd(maxPaddingName)}: ${value.nf().padStart(maxPaddingValue)}")
                )
            }
        }

        FileIo.writeFile("$OUTPUT_PATH/ev-building-list.txt", output.joinToString("\n"))
    }

    fun exportWonderEnterpriseValueData() {
        val output = mutableListOf<String>()

        val maxPaddingName = ap.gda.wonderBasedEv.maxOf { it.key.length }

        val maxPaddingValue = ap.gda.wonderBasedEv.maxOf {
            it.value.nf().length
        }

        ap.gda.wonderBasedEv.forEach { (name, value) ->
            output.add("${name.padEnd(maxPaddingName)}: ${value.nf().padStart(maxPaddingValue)}")
        }

        FileIo.writeFile("$OUTPUT_PATH/ev-wonder-list.txt", output.joinToString("\n"))
    }
}