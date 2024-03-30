package game.loader.game.tasks

import Logging
import game.loader.game.GameData
import game.loader.game.IGameData
import logger

/**
 * Add a reference to all buildings that produce a resource in the resources objects.
 */
class ProducerDetectorTask(
    private val gd: GameData
) : IGameData by gd, Logging {
    private val log = logger()

    fun process() {
        resources.values.forEach { r ->
            r.producer.clear()
            r.producer.addAll(
                buildings.filterValues { b -> b.output.containsKey(r) }.values
            )
        }
    }
}