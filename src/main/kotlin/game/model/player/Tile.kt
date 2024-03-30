package game.model.player

import game.model.game.Deposit
import utils.io.HasIdBase
import utils.svg.Point

class Tile(
    id: Int,
    val tile: Int,
    val explored: Boolean,
    val point: Point,
    val deposit: Map<String, Deposit>,
    val building: TileBuilding?
) : HasIdBase(id)