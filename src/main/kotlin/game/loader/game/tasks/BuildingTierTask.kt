package game.loader.game.tasks

import Logging
import game.common.BuildingType
import game.loader.game.GameData
import game.loader.game.IGameData
import logger

/**
 * Sets the tiers of [BuildingType.HQ] and [BuildingType.NATURAL_WONDER] to `0`.
 * Sets the tiers of [BuildingType.WORLD_WONDER] to `1`.
 */
class BuildingTierTask(
    private val gd: GameData
) : IGameData by gd, Logging {
    private val log = logger()

    fun process() {
        buildings.values.filter { it.tier == null }.forEach {
            if (it.type == BuildingType.WORLD_WONDER) {
                it.tier = 1
            } else if (it.type == BuildingType.NATURAL_WONDER || it.type == BuildingType.HQ) {
                it.tier = 0
            }
        }
    }
}