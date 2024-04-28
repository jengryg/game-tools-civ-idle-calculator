package analysis.visual

import analysis.visual.hexagons.Grid
import constants.SVG_DEFAULT_FONT_SIZE
import constants.SVG_HEXAGON_SIZE
import game.model.player.Tile
import utils.svg.DVIPSColors
import utils.svg.DVIPSColors.withAlpha
import utils.svg.Point
import utils.svg.SVGImage
import utils.svg.SVGQueuedText
import java.awt.Color
import kotlin.math.sqrt

open class BaseHexagonalMap(
    protected val mapSize: Int,
    protected val tiles: Map<Int, Tile>
) {
    protected val grid = Grid(mapSize, mapSize, SVG_HEXAGON_SIZE)

    val svg = grid.hexagons.flatten().let { p ->
        SVGImage(
            coordinateMaxX = p.maxOf { it.x } + sqrt(3.0) * SVG_HEXAGON_SIZE / 2.0,
            coordinateMaxY = p.maxOf { it.y } + sqrt(3.0) * SVG_HEXAGON_SIZE / 2.0,
        )
    }

    val tileIteration = grid.pointIteration.map { p -> tiles[p.toInt()]!! }

    fun color(color: Color) {
        svg.color(color)
    }

    fun fontSize(fontSize: Int) {
        svg.fontSize(fontSize)
    }

    fun hexagonToPixel(hexagon: Point): Point {
        return grid.gridToHex(hexagon).let { hex -> grid.layout.hexToPixel(hex) }
    }

    fun getPolygon(p: Point): List<Point> {
        return grid.layout.polygonCorners(
            grid.gridToHex(p)
        )
    }

    fun drawHexagonBoundaries(color: Color = DVIPSColors.Black, tl: Tile? = null) {
        color(color)

        if (tl == null) {
            tileIteration.forEach { tile ->
                svg.polygon(getPolygon(tile.point), filled = false)
            }
        } else {
            svg.polygon(getPolygon(tl.point), filled = false)
        }
    }

    fun drawHexagonSlice(
        color: Color = DVIPSColors.Black.withAlpha(128),
        tl: Tile,
        slices: IntRange
    ) {
        color(color)

        assert(slices.first >= 0 && slices.last <= 5)

        val slicePolygon = getPolygon(tl.point).slice(slices).let {
            // If the slice does not contain the triangles from 0 to 5, we need to add the center as additional point
            if (slices.last == 5) it else it.plus(
                hexagonToPixel(tl.point)
            )
        }
        svg.polygon(slicePolygon)
    }

    fun fillHexagon(color: Color = DVIPSColors.Transparent, tl: Tile) {
        color(color)

        svg.polygon(getPolygon(tl.point), filled = true)
    }

    fun hexagonCircle(
        color: Color = DVIPSColors.Transparent,
        tl: Tile,
        filled: Boolean = true,
        scale: Double = 0.25
    ) {
        color(color)
        svg.circle(
            center = hexagonToPixel(tl.point),
            radius = SVG_HEXAGON_SIZE.toDouble() * scale,
            filled = filled
        )
    }

    fun addFogOfWar(color: Color = DVIPSColors.Gray.withAlpha(64)) {
        tileIteration.forEach { tile ->
            if (!tile.explored) {
                fillHexagon(color, tile)
            }
        }
    }

    private val textBuffer = mutableListOf<SVGQueuedText>()

    fun writeToTextBuffer(
        color: Color = DVIPSColors.Black,
        fontSize: Int = SVG_DEFAULT_FONT_SIZE,
        string: String,
        hexagon: Point,
        xOffset: Double = 0.0,
        yOffset: Double = 0.0
    ): Int {
        textBuffer.add(
            SVGQueuedText(
                color = color,
                fontSize = fontSize,
                string = string,
                x = hexagon.x + xOffset,
                y = hexagon.y + yOffset
            )
        )

        return textBuffer.size
    }

    fun flushTextBuffer() {
        textBuffer.forEach {
            color(it.color)
            fontSize(it.fontSize)

            svg.write(it.string, it.x, it.y)
        }

        textBuffer.clear()
    }
}