package analysis.visual.hexagons

import Logging
import logger
import utils.svg.Point
import kotlin.math.roundToInt

class Grid(
    private val maxX: Int,
    private val maxY: Int,
    val size: Int
) : Logging {
    private val log = logger()

    val layout = Layout(size = Point(x = size, y = size))

    val center = Point(x = (maxX.toDouble() / 2.0), y = (maxY.toDouble() / 2.0))

    fun gridToHex(grid: Point): Hex {
        return OffsetCoord.roffsetToCube(Offset.ODD, OffsetCoord(grid.x.roundToInt(), grid.y.roundToInt()))
    }

    val pointIteration = (0 until maxX).flatMap { x ->
        (0 until maxY).map { y ->
            Point(x = x.toDouble(), y = y.toDouble())
        }
    }.filter { !isEdge(it) }

    val tileIteration = pointIteration.map { it.toInt() }

    fun isEdge(grid: Point): Boolean {
        return grid.x < 1 || grid.y < 1 || grid.x > this.maxX - 2 || grid.y > this.maxY - 2
    }

    val hexagons = pointIteration.map { p ->
        layout.polygonCorners(
            gridToHex(p)
        ).also {
            log.atDebug()
                .setMessage("Drawing Hexagon.")
                .addKeyValue("xy") { p }
                .addKeyValue("hexagon") { it }
                .log()
        }
    }
}