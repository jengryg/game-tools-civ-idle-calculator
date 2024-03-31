package game.loader.tasks

import Logging
import game.common.modifiers.ActiveBuildingMod
import game.common.modifiers.BuildingMod
import game.common.modifiers.BuildingModType
import game.loader.game.GameData
import game.loader.player.PlayerData
import logger

class ActiveBuildingModTask(
    private val gd: GameData,
    private val pd: PlayerData
) : Logging {
    private val log = logger()

    fun process() {
        gd.buildings.values.forEach { bld ->
            bld.mods.forEach { mod ->
                val multi = when (mod.type) {
                    BuildingModType.TECHNOLOGY -> checkTechnologyBuildingMod(mod)
                    BuildingModType.WONDER -> checkWonderBuildingMod(mod)
                    BuildingModType.GREAT_PERSON -> checkGreatPersonBuildingMod(mod)
                }
                if (multi > 0.0) {
                    bld.activeMods.add(
                        ActiveBuildingMod(
                            mod = mod,
                            note = "${mod.type} ${mod.from}: ${mod.value} ${mod.target} for ${mod.bldName} @ $multi",
                            effect = multi
                        )
                    )
                    log.atTrace()
                        .setMessage("Active modification for building.")
                        .addKeyValue("from") { mod.from }
                        .addKeyValue("building") { bld.name }
                        .addKeyValue("type") { mod.type }
                        .addKeyValue("target") { mod.target }
                        .addKeyValue("multi") { multi }
                        .log()
                }
            }
        }
    }

    private fun checkWonderBuildingMod(mod: BuildingMod): Double {
        return if (pd.tiles.values.any { it.building?.bld?.name == mod.from }) {
            mod.value
        } else {
            0.0
        }
    }

    private fun checkGreatPersonBuildingMod(mod: BuildingMod): Double {
        val obtained =
            pd.obtainedGreatPeople.values.singleOrNull { it.greatPerson.name == mod.from }

        if (obtained == null) {
            return 0.0
        }

        return obtained.totalEffect * obtained.greatPerson.value
    }

    private fun checkTechnologyBuildingMod(mod: BuildingMod): Double {
        return if (pd.unlockedTechnology.containsKey(mod.from)) {
            mod.value
        } else {
            0.0
        }
    }
}