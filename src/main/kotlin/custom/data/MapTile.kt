package custom.data

import game.data.Deposit
import game.hex.Point

class MapTile(
    val id: Int,
    val tile: Int,
    val explored: Boolean,
    val deposit: Map<String, Deposit>,
    val building: BuildBuilding?,
    val point: Point,
    val pointSvg: Point,
)