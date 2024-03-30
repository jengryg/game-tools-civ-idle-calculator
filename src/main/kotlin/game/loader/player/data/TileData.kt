package game.loader.player.data

import game.loader.game.data.DepositData
import utils.io.HasIdBase
import utils.svg.Point

class TileData(
    id: Int,
    val tile: Int,
    val explored: Boolean,
    val point: Point,
    val deposit: Map<String, DepositData> = emptyMap(),
    val building: TileBuildingData?,
) : HasIdBase(id)