package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.BuildingType
import game.common.modifiers.ActiveBuildingMod
import game.common.modifiers.BuildingMod
import game.common.modifiers.BuildingModTarget
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
    val cost: Map<Resource, Double>,
    val activeMods: List<ActiveBuildingMod>,
    val specialMods: MutableList<ActiveBuildingMod>,
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

    val ageTierDiff = unlockedBy?.let { it.age.id + 1 - tier } ?: -1


    val inputMulti
        get() = activeMods.plus(specialMods).filter { it.mod.target == BuildingModTarget.INPUT }.sumOf { it.effect }

    val outputMulti
        get() = activeMods.plus(specialMods).filter { it.mod.target == BuildingModTarget.OUTPUT }.sumOf { it.effect }

    val storageMulti
        get() = activeMods.plus(specialMods).filter { it.mod.target == BuildingModTarget.STORAGE }.sumOf { it.effect }

    val workerMulti
        get() = activeMods.plus(specialMods).filter { it.mod.target == BuildingModTarget.WORKER }.sumOf { it.effect }

    val effectiveOutput
        get() = output.mapValues { (_, a) -> a * (1.0 + outputMulti) }

    val effectiveInput
        get() = input.mapValues { (_, a) -> a * (1.0 + inputMulti) }

    val effectiveWorkers
        get() : Double {
            val workersForInput = effectiveInput.filterKeys { !it.isWorker && !it.isScience }.values.sum()
            val workersForOutput = effectiveOutput.filterKeys { !it.isWorker && !it.isScience }.values.sum()

            return (workersForInput + workersForOutput) / (1 + workerMulti)
        }
}