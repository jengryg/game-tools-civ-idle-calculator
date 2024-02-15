package game.data

import SVG_HEXAGON_SIZE
import game.grid.Grid

class City(
    val name: String,
    val size: Int,
    val deposits: List<DepositAmount>,
    val uniqueBuildings: List<Building>,
) {
    val grid = Grid(size, size, SVG_HEXAGON_SIZE)
}