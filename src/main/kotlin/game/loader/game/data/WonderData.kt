package game.loader.game.data

import game.common.modifiers.BuildingMod
import game.common.modifiers.BuildingModTarget
import utils.io.HasNameBase

class WonderData(
    name: String,
    val targetBuildingName: String,
    val multipliers: Map<BuildingModTarget, Double>
) : HasNameBase(name) {
    val mods: MutableList<BuildingMod> = mutableListOf()
}