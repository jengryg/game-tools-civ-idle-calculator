package game.model

import game.loader.game.GameData
import game.loader.player.PlayerData

interface IModel {
    val gd: GameData
    val pd: PlayerData
}