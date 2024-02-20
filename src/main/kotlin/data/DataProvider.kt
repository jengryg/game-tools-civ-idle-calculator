package data

import data.definitions.GameDefinition
import data.definitions.GameDefinitionFactory
import data.player.PlayerState
import data.player.PlayerStateFactory

object DataProvider {
    val gs: GameDefinition = GameDefinitionFactory().getGameDefinition()
    val ps: PlayerState = PlayerStateFactory(gs).getPlayState()
}