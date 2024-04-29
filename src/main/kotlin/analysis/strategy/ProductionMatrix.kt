package analysis.strategy

import game.model.game.Building
import game.model.game.Resource
import utils.tabular

class ProductionMatrix(
    val buildings: List<Building>,
    val resources: List<Resource>,
) {
    private val matrix = buildings.associateWith { b ->
        ResourceVector(resources).also { rv ->
            resources.forEach { r ->
                val output = b.effectiveOutput.getOrDefault(r, 0.0)
                val input = when (r.isWorker) {
                    false -> b.effectiveInput.getOrDefault(r, 0.0)
                    true -> b.effectiveWorkers
                }

                rv.set(r, output - input)
            }
        }
    }

    fun setValue(b: Building, r: Resource, value: Double) {
        matrix[b]!![r] = value
    }

    fun getValue(b: Building, r: Resource): Double {
        return matrix[b]!![r]
    }

    fun getOrDefault(b: Building, r: Resource, default: Double = 0.0): Double {
        return matrix[b]?.get(r) ?: default
    }

    /**
     * Adds the [other] production matrix to this one to create a matrix that uses the distinct union of buildings and
     * resources of this matrix and the [other] matrix as keys. If a [Building],[Resource] is not set in one of the two
     * matrices we use 0.0 as default value for that entry and just add the other value to it.
     *
     * @return a new instance containing the sum of this and the [other] matrix.
     */
    operator fun plus(other: ProductionMatrix): ProductionMatrix {
        val result = ProductionMatrix(
            buildings = buildings.plus(other.buildings).distinctBy { it.name },
            resources = resources.plus(other.resources).distinctBy { it.name },
        )

        result.buildings.forEach { b ->
            result.resources.forEach { r ->
                result.setValue(b, r, getOrDefault(b, r, 0.0) + other.getOrDefault(b, r, 0.0))
            }
        }

        return result
    }

    /**
     * [ProductionMatrix] multiplied from the left with [BuildingVector] produces the [ResourceVector] containing the
     * surpluses resp. deficits of the production configuration.
     *
     * @return a new instance containing the surplus resp. deficit vector of the production configuration.
     */
    operator fun times(bv: BuildingVector): ResourceVector {
        val result = ResourceVector(resources)

        buildings.forEach { b ->
            resources.forEach { r ->
                result[r] += matrix[b]!![r] * bv[b]
            }
        }

        return result
    }

    /**
     * Calculates the [ResourceVector] containing the total production consumption of the production configuration.
     *
     * @return a new instance containing the total production consumption of the production configuration.
     */
    fun consumption(bv: BuildingVector): ResourceVector {
        val result = ResourceVector(resources)

        buildings.forEach { b ->
            resources.forEach { r ->
                matrix[b]!![r].let { m ->
                    if (m > 0.0) {
                        result[r] += m * bv[b]
                    }
                }
            }
        }

        return result
    }

    /**
     * Calculates the [ResourceVector] containing the total production output of the production configuration.
     *
     * @return a new instance containing the total production output of the production configuration.
     */
    fun production(bv: BuildingVector): ResourceVector {
        val result = ResourceVector(resources)

        buildings.forEach { b ->
            resources.forEach { r ->
                matrix[b]!![r].let { m ->
                    if (m < 0.0) {
                        result[r] += m * bv[b]
                    }
                }
            }
        }

        return result
    }

    override fun toString(): String {
        val factories = buildings.filter { matrix[it]!!.nonZero() }

        return tabular(
            listOf(
                listOf(null).plus(factories.map { it.short })
            ).plus(
                resources.map { r -> listOf(r.short).plus(factories.map { b -> matrix[b]?.get(r)?.toString() }) }
            )
        )
    }
}