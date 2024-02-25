package analysis.enrichment

import Logging
import data.player.IPlayerState
import data.player.PlayerState
import logger

class PlayerStateAnalyser(
    val ps: PlayerState
) : IPlayerState by ps, Logging {
    private val log = logger()

}