package hexagons

import kotlin.math.cos
import kotlin.math.sin

class Layout(
    val orientation: Orientation = Orientation(),
    val size: Point,
    val origin: Point,
) {
    fun hexCornerOffset(corner: Int): Point {
        val angle = (2.0 * Math.PI * (orientation.startAngle - corner)) / 6.0;
        return Point(
            x = size.x * cos(angle),
            y = size.y * sin(angle)
        )
    }

    fun polygonCorners(h: Hex): List<Point> {
        val center = hexToPixel(h)
        return (0..5).map {
            hexCornerOffset(it).let { offset ->
                Point(x = center.x + offset.x, center.y + offset.y)
            }
        }
    }

    fun hexToPixel(h: Hex): Point {
        val x = (orientation.f0 * h.q + orientation.f1 * h.r) * size.x;
        val y = (orientation.f2 * h.q + orientation.f3 * h.r) * size.y;
        return Point(x = x + origin.x, y + origin.y)
    }
}