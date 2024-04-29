package analysis.strategy

import game.model.Model
import game.model.game.Building
import utils.tabular
import kotlin.math.max

class BuildingVector(
    private val buildings: List<Building>,
    private val initValue: Double = 0.0,
) {
    constructor(model: Model, initValue: Double = 0.0) : this(model.buildings.values.sortedByDescending { it.tier }
        .toList(), initValue)

    private val vector = buildings.associateWith { initValue }.toMutableMap()

    fun getBuildings(): List<Building> {
        return vector.keys.toList()
    }

    fun change(b: Building, v: Double): Double {
        vector[b] = max((vector[b] ?: 0.0) + v, 0.0)
        return vector[b]!!
    }

    operator fun get(b: Building): Double {
        return vector.getOrDefault(b, 0.0)
    }

    operator fun set(b: Building, value: Double) {
        vector[b] = value
    }

    operator fun plus(bv: BuildingVector): BuildingVector {
        val buildings = (getBuildings() + bv.getBuildings()).distinctBy { it.name }
        return BuildingVector(buildings, initValue).also {
            buildings.forEach { b ->
                it[b] = this[b] + bv[b]
            }
        }
    }

    operator fun times(factor: Double): BuildingVector {
        return BuildingVector(buildings, initValue).also {
            buildings.forEach { b ->
                it[b] = this[b] * factor
            }
        }
    }

    fun happy(evaluation: BuildingVector): Double {
        return vector.map { (b, v) ->
            evaluation[b] * freeType(b, v)
        }.sum()
    }

    private fun freeType(b: Building, v: Double): Double {
        return max(v - 1.0, 0.0)
    }

    override fun toString(): String {
        return tabular(buildings.map { listOf(it.short, vector[it].toString()) })
    }

    fun compact(): Map<Building, Double> {
        return vector.filterValues { it != 0.0 }
    }

    fun compactToString(): String {
        return compact().entries.joinToString {
            "${it.key.name} ${it.value}"
        }
    }

    companion object {
        fun just(b: Building, v: Double): BuildingVector {
            return BuildingVector(listOf()).apply {
                change(b, v)
            }
        }
    }
}