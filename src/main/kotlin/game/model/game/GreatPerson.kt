package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.modifiers.BuildingMod
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class GreatPerson(
    name: String,
    val value: Double,
    @JsonSerialize(using = JustNameSerializer::class)
    val age: Age,
    val mods: List<BuildingMod>,
) : HasNameBase(name)