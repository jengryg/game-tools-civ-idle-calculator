package game.loader.player

import Logging
import game.loader.player.json.SaveGameJson
import logger
import utils.io.FileIo
import utils.io.JsonParser

/**
 * Load the player state from the save game file located at [PlayerJsonLoaderConfiguration.playerInputDirectory].
 */
class PlayerJsonLoader(
    configuration: PlayerJsonLoaderConfiguration? = null
) : Logging {
    private val log = logger()

    private val cfg = configuration ?: PlayerJsonLoaderConfiguration().also {
        log.atInfo()
            .setMessage("Initialized ${this::class.simpleName} with default configuration.")
            .log()
    }

    fun load(): PlayerJson {
        val saveGame = loadSaveGame()

        log.atInfo()
            .setMessage("Completed loading player data.")
            .addKeyValue("directory") { cfg.playerInputDirectory }
            .log()

        return PlayerJson(
            saveGame = saveGame
        )
    }

    private fun loadSaveGame(): SaveGameJson =
        JsonParser.deserialize<SaveGameJson>(
            FileIo.readCompressedFile("${cfg.playerInputDirectory}/CivIdle").also {
                // The save game is compressed json.
                // We write the json to the same directory to make it available for inspection to the user.
                FileIo.writeFile("${cfg.playerOutputDirectory}/CivIdle.json", it)
            }
        )
}