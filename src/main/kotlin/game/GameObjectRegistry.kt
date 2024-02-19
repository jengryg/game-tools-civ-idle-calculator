package game

import IRON_FORGE_BOOST_TO_ADD_FROM_NEIGHBOR_IRON_MINE
import REFINERY_BOOST_TO_ADD_FROM_NEIGHBOR_OIL_SOURCE
import STEEL_MILL_BOOST_TO_ADD_FROM_NEIGHBOR_EIFFEL_TOWER
import WHEAT_BOOST_TO_ADD_FROM_NEIGHBOR_WATER_SOURCE
import game.data.*
import utils.FileIo
import utils.JsonParser
import utils.nf

class GameObjectRegistry(
    val ages: Map<String, Age>,
    val deposits: Map<String, Deposit>,
    val resources: Map<String, Resource>,
    val buildings: Map<String, Building>,
    val technologies: Map<String, Technology>,
    val cities: Map<String, City>,
    val persons: Map<String, GreatPerson>,
) {
    val wonders: Map<String, Wonder>

    init {
        GameDataCalculator.calculateTierAndPrice(this)
        wonders = initializeWonders()
    }

    val producers: Map<String, Map<String, Building>> = resources.values.associate { resource ->
        resource.name to buildings.filter { bld -> bld.value.output.containsKey(resource.name) }
    }

    val tierBasedEv: Map<Int, Map<String, String>> = buildings.values.filter { it.special == BuildingType.NORMAL }
        .mapNotNull { it.tier }.distinct().associateWith { tier ->
            buildings.filter { it.value.tier == tier && it.value.special == BuildingType.NORMAL }.values.associate {
                it.name to nf(
                    it.getCostForUpgradingLevels(
                        0,
                        1
                    ).values.sumOf { ra -> ra.enterpriseValue() })
            }
        }

    val wonderBasedEv: Map<String, String> =
        buildings.values.filter { it.special == BuildingType.WORLD_WONDER }.associate {
            it.name to nf(it.getCostForUpgradingLevels(0, 1).values.sumOf { ra -> ra.enterpriseValue() })
        }

    private fun initializeWonders(): Map<String, Wonder> {
        return buildings.filterValues { it.special == BuildingType.WORLD_WONDER || it.special == BuildingType.NATURAL_WONDER }
            .map {
                it.key to processWonder(it.value)
            }.toMap()
    }

    private fun processWonder(building: Building): Wonder {
        val boost = when (building.name) {
            "HatshepsutTemple" -> listOf(
                StandardBoost(
                    boostType = BoostType.OUTPUT,
                    boostTarget = buildings["WheatFarm"]!!,
                    value = WHEAT_BOOST_TO_ADD_FROM_NEIGHBOR_WATER_SOURCE
                )
            )


            "CircusMaximus" -> listOf(BoostType.OUTPUT, BoostType.STORAGE).flatMap { type ->
                listOf("MusiciansGuild", "PaintersGuild", "WritersGuild").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "PyramidOfGiza" -> buildings.values.filter { it.output.containsKey("Worker") }.map {
                StandardBoost(
                    boostType = BoostType.OUTPUT,
                    boostTarget = it,
                    value = 1.0
                )
            }

            "Parthenon" -> {
                listOf("MusiciansGuild", "PaintersGuild").map {
                    StandardBoost(
                        boostType = BoostType.WORKER,
                        boostTarget = buildings[it]!!
                    )
                }
            }

            "Stonehenge" -> buildings.values.filter { it.output.containsKey("Stone") || it.input.containsKey("Stone") }
                .map {
                    StandardBoost(
                        boostType = BoostType.OUTPUT,
                        boostTarget = it,
                        value = 1.0
                    )
                }

            "TerracottaArmy" -> listOf(BoostType.OUTPUT, BoostType.WORKER, BoostType.STORAGE).map {
                StandardBoost(
                    boostType = BoostType.WORKER,
                    boostTarget = buildings["IronMiningCamp"]!!
                )
            }.plus(
                StandardBoost(
                    boostType = BoostType.OUTPUT,
                    boostTarget = buildings["IronForge"]!!,
                    value = IRON_FORGE_BOOST_TO_ADD_FROM_NEIGHBOR_IRON_MINE
                )
            )

            "Persepolis" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                listOf("StoneQuarry", "LoggingCamp", "CopperMiningCamp").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "ForbiddenCity" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                listOf("PaperMaker", "WritersGuild", "PrintingHouse").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "HimejiCastle" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                listOf("CaravelBuilder", "GalleonBuilder", "FrigateBuilder").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "TempleOfArtemis" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                listOf("SwordForge", "Armory").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "EiffelTower" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).map {
                StandardBoost(
                    boostType = it,
                    boostTarget = buildings["SteelMill"]!!,
                    value = STEEL_MILL_BOOST_TO_ADD_FROM_NEIGHBOR_EIFFEL_TOWER
                )
            }

            "Rijksmuseum" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                buildings.values.filter { it.output.containsKey("Culture") || it.input.containsKey("Culture") }
                    .map { target ->
                        StandardBoost(
                            boostType = type,
                            boostTarget = target,
                            value = 1.0
                        )
                    }
            }

            "SummerPalace" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                buildings.values.filter { it.output.containsKey("Gunpowder") || it.input.containsKey("Gunpowder") }
                    .map { target ->
                        StandardBoost(
                            boostType = type,
                            boostTarget = target,
                            value = 1.0
                        )
                    }
            }


            "BrandenburgGate" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).map {
                StandardBoost(
                    boostType = it,
                    boostTarget = buildings["OilRefinery"]!!,
                    value = REFINERY_BOOST_TO_ADD_FROM_NEIGHBOR_OIL_SOURCE
                )
            }.plus(
                listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                    listOf("OilWell", "CoalMine").map { target ->
                        StandardBoost(
                            boostType = type,
                            boostTarget = buildings[target]!!,
                            value = 1.0
                        )
                    }
                }
            )

            else -> null
        }

        return Wonder(
            name = building.name,
            building = building,
            stdBoost = boost
        )
    }


    fun exportToJson() {
        FileIo.writeFile(
            file = "output/age.json",
            content = JsonParser.serialize(ages)
        )

        FileIo.writeFile(
            file = "output/deposits.json",
            content = JsonParser.serialize(deposits)
        )

        FileIo.writeFile(
            file = "output/resources.json",
            content = JsonParser.serialize(resources)
        )

        FileIo.writeFile(
            file = "output/buildings.json",
            content = JsonParser.serialize(buildings)
        )

        FileIo.writeFile(
            file = "output/technologies.json",
            content = JsonParser.serialize(technologies)
        )

        FileIo.writeFile(
            file = "output/cities.json",
            content = JsonParser.serialize(cities)
        )
        FileIo.writeFile(
            file = "output/persons.json",
            content = JsonParser.serialize(persons)
        )
        FileIo.writeFile(
            file = "output/producers.json",
            content = JsonParser.serialize(producers)
        )

        FileIo.writeFile(
            file = "output/tier-base-ev.json",
            content = JsonParser.serialize(tierBasedEv)
        )
        FileIo.writeFile(
            file = "output/wonder-base-ev.json",
            content = JsonParser.serialize(wonderBasedEv)
        )
    }
}