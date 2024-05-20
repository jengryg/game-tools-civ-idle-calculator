package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.modifiers.BuildingMod
import utils.io.HasNameBase
import utils.io.JustNameSerializer
import kotlin.math.pow

class Technology(
    name: String,
    val column: Int,
    @JsonSerialize(using = JustNameSerializer::class)
    val age: Age,
    @JsonSerialize(contentUsing = JustNameSerializer::class)
    val predecessor: List<Technology>,
    val mods: List<BuildingMod>,
) : HasNameBase(name) {
    val cost = 5.0.pow(age.id) * 1.5.pow(column) * 5_000
}