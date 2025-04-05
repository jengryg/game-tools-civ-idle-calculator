package game.loader.game.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
 import game.common.modifiers.BuildingMod
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class TechnologyData(
    name: String,
    val column: Int,
    @JsonSerialize(using = JustNameSerializer::class)
    val age: AgeData,
    @JsonSerialize(contentUsing = JustNameSerializer::class)
    val predecessor: List<TechnologyData>,
    val revealDepositNames: List<String>,
    val unlocksBuildingNames: List<String>,
    val buildingMultipliers: List<BuildingMultiplier>
) : HasNameBase(name) {
    val mods: MutableList<BuildingMod> = mutableListOf()
}