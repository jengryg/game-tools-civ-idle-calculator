package game.common.modifiers

open class BuildingMod(
    val from: String,
    val bldName: String,
    val type: BuildingModType,
    val target: BuildingModTarget,
    val value: Double = 0.0,
)