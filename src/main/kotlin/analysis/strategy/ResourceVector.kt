package analysis.strategy

import Logging
import game.model.Model
import game.model.game.Resource
import logger
import utils.tabular

class ResourceVector(
    private val resources: List<Resource>,
    private val initValue: Double = 0.0,
) : Logging {
    private val log = logger()

    constructor(model: Model, initValue: Double = 0.0) : this(model.resources.values.sortedByDescending { it.tier }.toList(), initValue)

    private val vector = resources.associateWith { initValue }.toMutableMap()

    fun getResources(): List<Resource> {
        return vector.keys.toList()
    }

    fun change(r: Resource, v: Double): Double {
        vector[r] = (vector[r] ?: 0.0) + v
        return vector[r]!!
    }

    operator fun get(r: Resource): Double {
        return vector.getOrDefault(r, 0.0)
    }

    operator fun set(r: Resource, value: Double) {
        vector[r] = value
    }

    operator fun plus(rv: ResourceVector): ResourceVector {
        val resources = (getResources() + rv.getResources()).distinctBy { it.name }
        return ResourceVector(resources, initValue).also {
            resources.forEach { r ->
                it[r] = this[r] + rv[r]
            }
        }
    }

    operator fun times(factor: Double): ResourceVector {
        return ResourceVector(resources, initValue).also {
            resources.forEach { r ->
                it[r] = this[r] * factor
            }
        }
    }

    fun isValid(): Boolean {
        val negatives = vector.filterValues { it < 0.0 }

        return (negatives.isEmpty()).also {
            log.atTrace()
                .setMessage("ResourceVector contains negatives.")
                .addKeyValue("resources") { negatives.keys.joinToString(",") }
                .log()
        }
    }

    fun nonZero(): Boolean {
        return vector.any { (_, a) -> a != 0.0 }
    }

    fun ev(): Double {
        return vector.entries.sumOf { (r, a) -> r.price * a }
    }

    override fun toString(): String {
        return tabular(resources.map { listOf(it.short, vector[it].toString()) })
    }

    fun compact(): Map<Resource, Double> {
        return vector.filterValues { it != 0.0 }
    }

    fun compactToString(): String {
        return compact().entries.joinToString {
            "${it.key.name}=${it.value}"
        }
    }

    /**
     * Choose the resource with the highest tier in the vector that currently is not sufficiently produced.
     */
    fun nextOptimizationElement(): Pair<Resource, Double>? {
        return vector.filterValues { it < 0.0 }.keys.firstOrNull()?.let {
            it to vector[it]!!
        }
    }
}