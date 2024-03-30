package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.BuildingType
import game.common.modifiers.BuildingMod
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class Building(
    name: String,
    val input: Map<Resource, Double>,
    val output: Map<Resource, Double>,
    val construction: Map<Resource, Double>,
    @JsonSerialize(contentUsing = JustNameSerializer::class)
    val deposit: List<Deposit>,
    val type: BuildingType,
    @JsonSerialize(using = JustNameSerializer::class)
    val unlockedBy: Technology?,
    val mods: List<BuildingMod>,
    val tier: Int,
    val cost: Map<Resource, Double>
) : HasNameBase(name) {

}