package data.definitions.extension

import common.BoostType
import common.StandardBoost
import data.definitions.GameDefinition
import data.definitions.model.Building
import data.definitions.model.Wonder

class WonderDataFactory(
    private val gd: GameDefinition
) {
    companion object {
        const val WHEAT_BOOST_TO_ADD_FROM_NEIGHBOR_WATER_SOURCE = 1.0
        const val IRON_FORGE_BOOST_TO_ADD_FROM_NEIGHBOR_IRON_MINE = 1.0
        const val STEEL_MILL_BOOST_TO_ADD_FROM_NEIGHBOR_EIFFEL_TOWER = 1.0
        const val REFINERY_BOOST_TO_ADD_FROM_NEIGHBOR_OIL_SOURCE = 1.0
    }

    fun getWonderEffects(): Map<String, Wonder> {
        return gd.buildings.filterValues { it.special.isAnyWonder }
            .map { it.key to processWonder(it.value) }.toMap()
    }

    private fun processWonder(building: Building): Wonder {
        val boost = when (building.name) {
            "HatshepsutTemple" -> listOf(
                StandardBoost(
                    boostType = BoostType.OUTPUT,
                    boostTarget = gd.buildings["WheatFarm"]!!,
                    value = WHEAT_BOOST_TO_ADD_FROM_NEIGHBOR_WATER_SOURCE
                )
            )


            "CircusMaximus" -> listOf(BoostType.OUTPUT, BoostType.STORAGE).flatMap { type ->
                listOf("MusiciansGuild", "PaintersGuild", "WritersGuild").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = gd.buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "PyramidOfGiza" -> gd.buildings.values.filter { it.output.containsKey("Worker") }.map {
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
                        boostTarget = gd.buildings[it]!!
                    )
                }
            }

            "Stonehenge" -> gd.buildings.values.filter { it.output.containsKey("Stone") || it.input.containsKey("Stone") }
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
                    boostTarget = gd.buildings["IronMiningCamp"]!!
                )
            }.plus(
                StandardBoost(
                    boostType = BoostType.OUTPUT,
                    boostTarget = gd.buildings["IronForge"]!!,
                    value = IRON_FORGE_BOOST_TO_ADD_FROM_NEIGHBOR_IRON_MINE
                )
            )

            "Persepolis" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                listOf("StoneQuarry", "LoggingCamp", "CopperMiningCamp").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = gd.buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "ForbiddenCity" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                listOf("PaperMaker", "WritersGuild", "PrintingHouse").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = gd.buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "HimejiCastle" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                listOf("CaravelBuilder", "GalleonBuilder", "FrigateBuilder").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = gd.buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "TempleOfArtemis" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                listOf("SwordForge", "Armory").map { target ->
                    StandardBoost(
                        boostType = type,
                        boostTarget = gd.buildings[target]!!,
                        value = 1.0
                    )
                }
            }

            "EiffelTower" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).map {
                StandardBoost(
                    boostType = it,
                    boostTarget = gd.buildings["SteelMill"]!!,
                    value = STEEL_MILL_BOOST_TO_ADD_FROM_NEIGHBOR_EIFFEL_TOWER
                )
            }

            "Rijksmuseum" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                gd.buildings.values.filter { it.output.containsKey("Culture") || it.input.containsKey("Culture") }
                    .map { target ->
                        StandardBoost(
                            boostType = type,
                            boostTarget = target,
                            value = 1.0
                        )
                    }
            }

            "SummerPalace" -> listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                gd.buildings.values.filter { it.output.containsKey("Gunpowder") || it.input.containsKey("Gunpowder") }
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
                    boostTarget = gd.buildings["OilRefinery"]!!,
                    value = REFINERY_BOOST_TO_ADD_FROM_NEIGHBOR_OIL_SOURCE
                )
            }.plus(
                listOf(BoostType.OUTPUT, BoostType.STORAGE, BoostType.WORKER).flatMap { type ->
                    listOf("OilWell", "CoalMine").map { target ->
                        StandardBoost(
                            boostType = type,
                            boostTarget = gd.buildings[target]!!,
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