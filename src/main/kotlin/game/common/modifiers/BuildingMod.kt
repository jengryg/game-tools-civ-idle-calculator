package game.common.modifiers

class BuildingMod(
    val note: String,
    val bldName: String,
    val type: BuildingModType,
    val target: BuildingModTarget,
    val value: Double = 0.0
)