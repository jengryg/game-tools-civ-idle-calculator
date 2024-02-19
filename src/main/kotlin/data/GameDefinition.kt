package data

import IRON_FORGE_BOOST_TO_ADD_FROM_NEIGHBOR_IRON_MINE
import REFINERY_BOOST_TO_ADD_FROM_NEIGHBOR_OIL_SOURCE
import STEEL_MILL_BOOST_TO_ADD_FROM_NEIGHBOR_EIFFEL_TOWER
import WHEAT_BOOST_TO_ADD_FROM_NEIGHBOR_WATER_SOURCE
import common.BoostType
import common.BuildingType
import common.StandardBoost
import data.model.definitions.*
import game.GameDataCalculator

class GameDefinition(
    override val ages: Map<String, Age>,
    override val deposits: Map<String, Deposit>,
    override val resources: Map<String, Resource>,
    override val buildings: Map<String, Building>,
    override val technologies: Map<String, Technology>,
    override val cities: Map<String, City>,
    override val persons: Map<String, GreatPerson>,
) : IGameDefinition {
    val wonders: Map<String, Wonder>

    init {
        GameDataCalculator.calculateTierAndPrice(this)
        wonders = initializeWonders()
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
}