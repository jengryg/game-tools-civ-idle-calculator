package game.loader.game.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.BuildingType
import game.common.modifiers.BuildingMod
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class BuildingData(
    name: String,
    val input: Map<ResourceData, Double>,
    val output: Map<ResourceData, Double>,
    val construction: Map<ResourceData, Double>,
    @JsonSerialize(contentUsing = JustNameSerializer::class)
    val deposit: List<DepositData>,
    val type: BuildingType,
    @JsonSerialize(using = JustNameSerializer::class)
    val unlockedBy: TechnologyData?,
    @JsonSerialize(using = JustNameSerializer::class)
    val specialCity: CityData?,
    val mods: MutableList<BuildingMod>,
) : HasNameBase(name) {
    var tier: Int? = null

    val cost: MutableMap<ResourceData, Double> = mutableMapOf()
}