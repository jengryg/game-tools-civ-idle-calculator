package utils.svg

import kotlin.math.roundToInt

class Point(
    override val x: Double,
    override val y: Double
) : XY {
    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

    constructor(int: Int) : this(
        x = ((int shr 16) and 0xffff).toDouble(),
        y = (int and 0xffff).toDouble()
    )

    override fun toString(): String {
        return "$x $y"
    }

    fun toInt(): Int {
        return (x.roundToInt() shl 16) + y.roundToInt()
    }
}