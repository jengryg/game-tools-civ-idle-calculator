package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.modifiers.BuildingMod
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class Technology(
    name: String,
    val column: Int,
    @JsonSerialize(using = JustNameSerializer::class)
    val age: Age,
    @JsonSerialize(contentUsing = JustNameSerializer::class)
    val predecessor: List<Technology>,
    val mods: List<BuildingMod>,
) : HasNameBase(name)