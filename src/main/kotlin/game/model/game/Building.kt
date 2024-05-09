package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.BuildingType
import game.common.modifiers.ActiveBuildingMod
import game.common.modifiers.BuildingMod
import game.common.modifiers.BuildingModTarget
import game.common.modifiers.BuildingModType
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
    private val customMods: MutableList<ActiveBuildingMod> = mutableListOf()

    fun addCustomMod(from: String, type: BuildingModType, target: BuildingModTarget, totalEffect: Double) {
        val mod = BuildingMod(
            from = from,
            bldName = name,
            type = type,
            target = target,
            value = 0.0
        )
        customMods.add(
            ActiveBuildingMod(
                mod = mod,
                note = "CustomMod of type $type applied to $target coming from $from with a total effect of $totalEffect",
                effect = totalEffect
            )
        )
    }

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

    val inputMods
        get() = activeMods.plus(specialMods).plus(customMods).filter { it.mod.target == BuildingModTarget.INPUT }

    val outputMods
        get() = activeMods.plus(specialMods).plus(customMods).filter { it.mod.target == BuildingModTarget.OUTPUT }

    val storageMods
        get() = activeMods.plus(specialMods).plus(customMods).filter { it.mod.target == BuildingModTarget.STORAGE }

    val workerMods
        get() = activeMods.plus(specialMods).plus(customMods).filter { it.mod.target == BuildingModTarget.WORKER }

    val inputMulti
        get() = inputMods.sumOf { it.effect }

    val outputMulti
        get() = outputMods.sumOf { it.effect }

    val storageMulti
        get() = storageMods.sumOf { it.effect }

    val workerMulti
        get() = workerMods.sumOf { it.effect }

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