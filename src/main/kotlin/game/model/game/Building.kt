package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.BuildingType
import game.common.modifiers.BuildingMod
import utils.io.HasNameBase
import utils.io.JustNameSerializer
import kotlin.math.pow

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
    fun getCostForOneLevel(level: Int): Map<Resource, Double> {
        val levelFactor = when (type) {
            BuildingType.NORMAL -> 1.5.pow(level)
            BuildingType.WORLD_WONDER -> 1.0
            else -> 0.0
        }
        return cost.mapValues { (_, a) -> a * levelFactor }.filterValues { it > 0.0 }
    }

    fun getTotalCostForLevels(fromLevel: Int, toLevel: Int): Map<Resource, Double> {
        val result = cost.mapValues { 0.0 }.toMutableMap()

        for (i in fromLevel until toLevel) {
            getCostForOneLevel(i).forEach { (r, d) ->
                result[r] = result[r]!! + d
            }
        }

        return result.filterValues { it > 0.0 }
    }
}