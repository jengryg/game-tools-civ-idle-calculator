package game.loader.game.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.modifiers.BuildingMod
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class GreatPersonData(
    name: String,
    val value: Double,
    @JsonSerialize(using = JustNameSerializer::class)
    val age: AgeData,
    val buildingMultipliers: List<Triple<String, String, Double>>
) : HasNameBase(name) {
    val mods: MutableList<BuildingMod> = mutableListOf()
}