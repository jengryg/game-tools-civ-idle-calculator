package analysis.strategy

import Logging
import constants.DEFAULT_OUTPUT_PATH
import game.model.Model
import logger
import utils.io.FileIo
import utils.nfd
import kotlin.math.ceil

/**
 * Simple implementation to find a [BuildingVector] that produces a valid production configuration for the given target
 * [BuildingVector]. The algorithm will initialize a solution [BuildingVector] that starts with the given [target].
 * It then adds producers to satisfy the solution vectors demands until all demands are satisfied.
 *
 * The producer building for each resource is selected using the [Model.getProducer] method.
 * The algorithm traverses the production tree using [ResourceVector.nextOptimizationElement] to select the resource
 * it has to produce at each step.
 */
class SimplexAlgorithm(
    private val name: String,
    private val model: Model,
    private val target: BuildingVector,
    private val happyness: BuildingVector,
    private val ceilingScale: Double = 10.0
) : Logging {
    private val log = logger()

    private val matrix: ProductionMatrix = ProductionMatrix(
        model.buildings.values.sortedByDescending { it.tier }.toList(),
        model.resources.values.sortedByDescending { it.tier }.toList()
    ).also {
        println(it)
    }

    private var solution = BuildingVector(
        model.buildings.values.sortedByDescending { it.tier }.toList(),
    ).plus(target)

    init {
        log.atDebug()
            .setMessage("Initialized ${this::class.simpleName} with building vector.")
            .addKeyValue("buildings") {
                solution.compactToString()
            }
            .log()
    }

    fun solve() {
        var done = false
        while (!done) {
            done = step()
        }
    }

    fun export(directory: String = "strategy") {
        FileIo.writeTable(
            "$DEFAULT_OUTPUT_PATH/$directory/$name.txt",
            solution.compact().filterValues { it > 0.0 }.map {
                listOf(it.key.name, it.value.nfd())
            }.plusElement(
                listOf("", "")
            ).plusElement(
                listOf("Total", solution.happy(happyness).nfd())
            )
        )
    }

    private fun step(): Boolean {
        val r = matrix * solution

        if (r.isValid()) {
            finish(r)
            return true
        }

        val (nextResource, amount) = r.nextOptimizationElement()
            ?: throw IllegalStateException("Next element is null, but resource vector is not valid.")

        val producer = model.getProducer(nextResource)
        val required = ceil(-ceilingScale * amount / producer.effectiveOutput[nextResource]!!) / ceilingScale

        log.atDebug()
            .setMessage("Step ${this::class.simpleName} result resource vector.")
            .addKeyValue("resources") { r.compactToString() }
            .addKeyValue("next") { nextResource.name }
            .addKeyValue("producer") { producer.name }
            .addKeyValue("output") { producer.effectiveOutput[nextResource] }
            .addKeyValue("required") { required }
            .log()

        solution.change(producer, required)

        return false
    }

    private fun finish(r: ResourceVector) {
        log.atDebug()
            .setMessage("Finished ${this::class.simpleName} with solution.")
            .addKeyValue("happiness") { solution.happy(happyness) }
            .addKeyValue("buildings") {
                solution.compactToString()
            }
            .addKeyValue("resources") {
                r.compactToString()
            }
            .log()

        println(solution.compactToString())
        println(matrix.production(solution).compactToString())
        println(matrix.consumption(solution).compactToString())
    }
}