package data.definitions.model

import SVG_HEXAGON_SIZE
import common.DepositAmount
import hexagons.Grid

class City(
    val name: String,
    val size: Int,
    val deposits: List<DepositAmount>,
    val uniqueBuildings: List<Building>,
) {
    val grid = Grid(size, size, SVG_HEXAGON_SIZE)
}