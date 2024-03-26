package analysis.enrichment

import Logging
import common.BuildingType
import data.definitions.GameDefinition
import data.definitions.IGameDefinition
import data.definitions.model.Building
import logger
import kotlin.math.cbrt
import kotlin.math.pow

class GameDefinitionAnalyser(
    val gd: GameDefinition
) : IGameDefinition by gd, Logging {
    private val log = logger()

    private val ignoreProducers = listOf("Blacksmith", "Shrine", "Church", "Cathedral", "HydroDam")

    val producers: Map<String, Map<String, Building>> = resources.values.associate { resource ->
        if (resource.name == "Faith") {
            // manual enrichment of the faith production by routing everything to StPetersBasilica since it is a faith fountain
            resource.name to buildings.filter { bld -> bld.value.name == "StPetersBasilica" }.also {
                log.atInfo()
                    .setMessage("Faith Producer selected as StPetersBasilica.")
                    .addKeyValue("resource") { resource.name }
                    .addKeyValue("producers") { buildings.keys.toSet() }
                    .log()
            }
        } else {
            resource.name to buildings.filter { bld -> bld.value.output.containsKey(resource.name) }.let { buildings ->
                if (buildings.size > 1) {
                    log.atInfo()
                        .setMessage("Multiple Producers found.")
                        .addKeyValue("resource") { resource.name }
                        .addKeyValue("producers") { buildings.keys.toSet() }
                        .log()

                    buildings.filterKeys { it !in ignoreProducers }.also {
                        if (it.size > 1) {
                            log.atWarn()
                                .setMessage("Could not resolve multiple producer issue. First ist used.")
                                .addKeyValue("resource") { resource.name }
                                .addKeyValue("producers") { it.keys.toSet() }
                                .log()
                        } else {
                            log.atDebug()
                                .setMessage("Multiple Producers resolved.")
                                .addKeyValue("resource") { resource.name }
                                .addKeyValue("producers") { it.keys.toSet() }
                                .log()
                        }
                    }
                } else {
                    buildings
                }
            }
        }

    }

    val tierBasedEv: Map<Int, Map<String, Double>> = buildings.values.filter { it.special == BuildingType.NORMAL }
        .mapNotNull { it.tier }.distinct().associateWith { tier ->
            buildings.filter { it.value.tier == tier && it.value.special == BuildingType.NORMAL }.values.associate {
                it.name to
                        it.getCostForUpgradingLevelsFromTo(
                            0,
                            1
                        ).values.sumOf { ra -> ra.enterpriseValue() }
            }
        }

    val wonderBasedEv: Map<String, Double> =
        buildings.values.filter { it.special == BuildingType.WORLD_WONDER }.associate {
            it.name to it.getCostForUpgradingLevelsFromTo(0, 1).values.sumOf { ra -> ra.enterpriseValue() }
        }

    fun getExtraGP(enterpriseValue: Double): Double {
        return cbrt(enterpriseValue / 1_000_000.0) / 4
    }

    fun getEnterpriseValueForGP(gpCount: Double): Double {
        return (4.0 * gpCount).pow(3) * 1_000_000
    }
}