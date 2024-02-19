package game.hex

import utils.XY
import kotlin.math.roundToInt

class Point(
    override val x: Double,
    override val y: Double
) : XY {
    override fun toString(): String {
        return "$x $y"
    }

    companion object {
        fun toTileInteger(point: Point): Int {
            return (point.x.roundToInt() shl 16) + point.y.roundToInt()
        }

        fun fromTileInteger(tile: Int): Point {
            return Point(
                x = ((tile shr 16) and 0xffff).toDouble(),
                y = (tile and 0xffff).toDouble()
            )
        }
    }
}