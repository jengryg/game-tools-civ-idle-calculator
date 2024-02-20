package data.player.model

import data.definitions.model.Deposit
import hexagons.Point

class MapTile(
    val id: Int,
    val tile: Int,
    val explored: Boolean,
    val deposit: Map<String, Deposit>,
    val building: BuildBuilding?,
    val point: Point,
    val pointSvg: Point,
)