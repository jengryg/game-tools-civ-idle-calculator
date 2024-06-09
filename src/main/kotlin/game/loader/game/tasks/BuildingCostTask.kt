package game.loader.game.tasks

import Logging
import game.common.BuildingType
import game.loader.game.GameData
import game.loader.game.IGameData
import game.loader.game.data.BuildingData
import logger
import kotlin.math.pow

/**
 * Calculates the construction cost of [BuildingType.NORMAL] and [BuildingType.WORLD_WONDER].
 * Only the cost of the first level is calculated and stored in the [BuildingData].
 */
class BuildingCostTask(
    private val gd: GameData
) : IGameData by gd, Logging {
    private val log = logger()

    fun process() {
        buildings.values.forEach {
            when (it.type) {
                BuildingType.NORMAL -> processNormalBuilding(it)
                BuildingType.WORLD_WONDER -> processWorldWonder(it)
                else -> Unit
            }
        }
    }

    private fun processNormalBuilding(b: BuildingData) {
        b.cost.clear()

        b.cost.putAll(
            b.construction.ifEmpty { b.input }.mapValues { (_, a) ->
                10.0 * a
            }
        )
    }

    private fun processWorldWonder(b: BuildingData) {
        b.cost.clear()

        val m = getWorldWonderCostMultiplier(b)
        b.cost.putAll(
            b.construction.ifEmpty { b.input }.mapValues { (r, a) ->
                (m * a) / (r.price ?: 1.0)
            }
        )
    }

    private fun getWorldWonderCostMultiplier(b: BuildingData): Double {
        val tech = b.unlockedBy.also {
            if (it == null) {
                log.atWarn()
                    .setMessage("Wonder ${b.name} is not unlocked by a tech.")
                    .log()
            }
        } ?: return 0.0
        // TODO: this needs to be adjusted to the religion based wonder unlocks.
        // ?: throw IllegalArgumentException("Wonder ${b.name} is not unlocked by a tech.")
        val aId = tech.age.id.toDouble()
        val tId = tech.column.toDouble()

        return 300.0 +
                10 * aId.pow(3) * tId.pow(2) +
                (100 * 5.0.pow(aId) * 1.5.pow(tId)) / tId.pow(2)
    }
}