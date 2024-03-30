package game.loader

import game.loader.game.*
import game.loader.player.*

class DataLoader(
    private val gameLoaderCfg: GameJsonLoaderConfiguration? = null,
    private val gameConverterCfg: GameConverterConfiguration? = null,
    private val playerLoaderCfg: PlayerJsonLoaderConfiguration? = null,
    private val playerConverterCfg: PlayerConverterConfiguration? = null
) {
    private fun loadGameData(): GameData {
        val gameJson = GameJsonLoader(gameLoaderCfg).load()
        return GameConverter(gameConverterCfg).convert(gameJson)
    }

    private fun loadPlayerData(gd: GameData): PlayerData {
        val playerJson = PlayerJsonLoader(playerLoaderCfg).load()
        return PlayerConverter(playerConverterCfg, gd).convert(playerJson)
    }

    fun loadCombinedData(): Pair<GameData, PlayerData> {
        val gd = loadGameData()
        val ps = loadPlayerData(gd)

        return Pair(gd, ps).apply {
            // TODO apply combined tasks
        }
    }
}