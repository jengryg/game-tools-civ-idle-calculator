package analysis.processors.citymap

import SVG_DEFAULT_FONT_SIZE
import SVG_HEXAGON_SIZE
import data.definitions.model.City
import data.player.model.MapTile
import hexagons.Point
import utils.DVIPSColors
import utils.DVIPSColors.withAlpha
import utils.SVGImage
import utils.SVGQueuedText
import java.awt.Color
import kotlin.math.sqrt

open class BaseHexagonalMap(
    val city: City,
    val tiles: Map<Int, MapTile>
) {
    val svg = city.grid.hexagons.flatten().let { p ->
        SVGImage(
            coordinateMaxX = p.maxOf { it.x } + sqrt(3.0) * SVG_HEXAGON_SIZE / 2.0,
            coordinateMaxY = p.maxOf { it.y } + sqrt(3.0) * SVG_HEXAGON_SIZE / 2.0,
        )
    }

    fun hexagonToPixel(hexagon: Point) : Point {
        return city.grid.gridToHex(hexagon).let { hex -> city.grid.layout.hexToPixel(hex) }
    }

    fun color(color: Color) {
        svg.color(color)
    }

    fun fontSize(fontSize: Int) {
        svg.fontSize(fontSize)
    }

    fun getPolygon(p: Point): List<Point> {
        return city.grid.layout.polygonCorners(
            city.grid.gridToHex(p)
        )
    }

    fun drawHexagonBoundaries(color: Color = DVIPSColors.Black, mapTile: MapTile? = null) {
        color(color)

        if (mapTile == null) {
            city.grid.pointIteration.map { p ->
                val tile = tiles[Point.toTileInteger(p)]!!
                svg.polygon(getPolygon(tile.point), filled = false)
            }
        } else {
            svg.polygon(getPolygon(mapTile.point), filled = false)
        }
    }

    fun drawHexagonSlice(
        color: Color = DVIPSColors.Black.withAlpha(128),
        mapTile: MapTile,
        slices: IntRange
    ) {
        color(color)

        assert(slices.first >= 0 && slices.last <= 5)

        val slicePolygon = getPolygon(mapTile.point).slice(slices).let {
            // If the slice does not contain the triangles from 0 to 5, we need to add the center as additional point
            if (slices.last == 5) it else it.plus(
                hexagonToPixel(mapTile.point)
            )
        }
        svg.polygon(slicePolygon)
    }

    fun fillHexagon(color: Color = DVIPSColors.Transparent, mapTile: MapTile) {
        color(color)

        svg.polygon(getPolygon(mapTile.point), filled = false)
    }

    fun hexagonCircle(
        color: Color = DVIPSColors.Transparent,
        mapTile: MapTile,
        filled: Boolean = true,
        scale: Double = 0.25
    ) {
        color(color)
        svg.circle(center = hexagonToPixel(mapTile.point), radius = SVG_HEXAGON_SIZE.toDouble() * scale, filled = filled)
    }

    fun addFogOfWar(color: Color = DVIPSColors.Gray.withAlpha(64)) {
        city.grid.pointIteration.map {
            val tile = tiles[Point.toTileInteger(it)]!!
            if (tile.explored) {
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
    }
}