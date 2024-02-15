package svg

import Logging
import logger
import org.apache.batik.dom.GenericDOMImplementation
import org.apache.batik.svggen.SVGGraphics2D
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.io.path.writer
import kotlin.math.roundToInt

class SVGImage(
    private val baseName: String = "",
    private val coordinateMaxX: Double,
    private val coordinateMaxY: Double,
) : Logging {
    private val log = logger()

    private val doc = GenericDOMImplementation
        .getDOMImplementation()
        .createDocument("https://www.w3.org/2000/svg", "svg", null)

    private val width = pV(coordinateMaxX)
    private val height = pV(coordinateMaxY)

    private val svg: SVGGraphics2D = SVGGraphics2D(doc).apply {
        svgCanvasSize = Dimension(width, height)
    }

    private val creationStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).replace(":", "-")

    init {
        svg.color = DVIPSColors.Black
        svg.font = Font("TimesRoman", Font.PLAIN, 26)

        log.atDebug()
            .setMessage("Initialized SvgImage.")
            .addKeyValue("baseName", baseName)
            .addKeyValue("width") { width }
            .addKeyValue("height") { height }
            .addKeyValue("creationTimeStamp", creationStamp)
            .log()
    }

    /**
     * Set the font size of the underlying [svg] to the given [size].
     */
    fun fontSize(size: Int) {
        svg.font = Font(svg.font.name, svg.font.style, size)
    }

    /**
     * Set the current drawing color of the underlying [svg] to the given [color].
     */
    fun color(color: Color) {
        svg.color = color
    }

    /**
     * Incremented every time the [export] method is used to enumerate the files when no specific filename is given to
     * the [export] method.
     */
    private var exportId = 0

    /**
     * Export the current drawings using [SVGGraphics2D.stream] to the given [java.io.OutputStreamWriter].
     */
    fun export(name: String? = null, directory: String = "output"): Path {
        return when {
            name.isNullOrBlank() -> "${baseName}_${exportId++}_${creationStamp}_${UUID.randomUUID()}.svg"
            else -> "${name}.svg"
        }.let { fileName ->
            Path.of(directory, fileName).also {
                it.writer(StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
                    .use { osw ->
                        svg.stream(osw, false)
                    }

                log.atDebug()
                    .setMessage("Saved SvgImage")
                    .addKeyValue("fileName", fileName)
                    .log()
            }
        }
    }

    /**
     * Draw a circle with midXY in [center] and radius given by [radius].
     *
     * If [filled] is set to true, the circle is filled with the current drawing color of the [svg],
     * otherwise only the outline is drawn in that color and the shape itself is transparent.
     */
    fun circle(center: XY, radius: Double, filled: Boolean = true) {
        if (filled) {
            svg.fillOval(
                pV(transformationX(center.x - radius)),
                pV(transformationY(center.y + radius)),
                pV(2 * radius),
                pV(2 * radius)
            )
        } else {
            svg.drawOval(
                pV(transformationX(center.x - radius)),
                pV(transformationY(center.y + radius)),
                pV(2 * radius),
                pV(2 * radius)
            )
        }
    }

    /**
     * Draw a rectangle with [lowerBounds] and [upperBounds].
     *
     * If [filled] is set to true, the rectangle is filled with the current drawing color of the [svg],
     * otherwise only the outline is drawn in that color and the shape itself is transparent.
     */
    fun rectangle(lowerBounds: XY, upperBounds: XY, filled: Boolean = true) {
        if (filled) {
            svg.fillRect(
                pV(transformationX(lowerBounds.x)),
                pV(transformationY(lowerBounds.y)),
                pV(upperBounds.x - lowerBounds.x),
                pV(upperBounds.y - lowerBounds.y)
            )
        } else {
            svg.drawRect(
                pV(transformationX(lowerBounds.x)),
                pV(transformationY(lowerBounds.y)),
                pV(upperBounds.x - lowerBounds.x),
                pV(upperBounds.y - lowerBounds.y)
            )
        }
    }

    /**
     * Draws a line from [startXY] to [endXY].
     */
    fun line(startXY: XY, endXY: XY) {
        svg.drawLine(
            pV(transformationX(startXY.x)),
            pV(transformationY(startXY.y)),
            pV(transformationX(endXY.x)),
            pV(transformationY(endXY.y))
        )
    }

    /**
     * Draw a line from the start XY given by its x,y coordinates [startX], [startY] to the end XY given by
     * its x,y coordinates [endX], [endY]. The x and y coordinate must correspond to .x resp. .y in the
     * original XYs coordinates.
     */
    fun line(startX: Double, startY: Double, endX: Double, endY: Double) {
        svg.drawLine(
            pV(transformationX(startX)),
            pV(transformationY(startY)),
            pV(transformationX(endX)),
            pV(transformationY(endY))
        )
    }

    /**
     * Write the given [string] at the given position.
     * The baseline of the first character is at the position ([x], [y]).
     */
    fun write(string: String, x: Double, y: Double) {
        svg.drawString(
            string,
            pV(transformationX(x)),
            pV(transformationY(y))
        )
    }

    /**
     * Write the given [string] at the given [position].
     * The baseline of the first character is at the position (x,y), where x and y are selected from [position] using
     * .x resp. .y.
     */
    fun write(string: String, position: XY) {
        write(string = string, x = position.x, y = position.y)
    }

    /**
     * Draw a polygon given by its [vertices].
     *
     * If [filled] is set to true, the polygon is filled with the current drawing color of the [svg],
     * otherwise only the outline is drawn in that color and the shape itself is transparent.
     */
    fun polygon(vertices: List<XY>, filled: Boolean = true) {
        if (vertices.size <= 2) {
            throw IllegalArgumentException("Polygon required at least 3 vertices.")
        }

        val xXYs = vertices.map { pV(transformationX(it.x)) }.toIntArray()
        val yXYs = vertices.map { pV(transformationY(it.y)) }.toIntArray()

        if (filled) {
            svg.fillPolygon(xXYs, yXYs, xXYs.size)
        } else {
            svg.drawPolygon(xXYs, yXYs, xXYs.size)
        }
    }


    /**
     * Value conversion from [Double] to the [Int] based pixel value the [svg] uses.
     */
    private fun pV(value: Double) = (value * 1).roundToInt()

    /**
     * The x-coordinate linear transformation to map a cartesian coordinate system to the svg grid.
     * The svg grid has `(0,0)` in the top left corner and `(width, height)` in the bottom right corner.
     *
     * Since we want to have a linear transformation for the x coordinate, that is independent of the y coordinate, this
     * map has the form `Tx(v) = v * m + b`, where `m` is the slope and `b` is the offset in `v = 0`.
     *
     * The positions of the corners require that `Tx(minX) = 0` and `Tx(maxX) = maxX - minX`, thus it follows that
     * `Tx(v) = v * 1 - minX`.
     */
    private fun transformationX(value: Double): Double {
        return value
    }

    /**
     * The y-coordinate linear transformation to map a cartesian coordinate system to the svg grid.
     * The svg grid has `(0,0)` in the top left corner and `(width, height)` in the bottom right corner.
     *
     * Since we want to have a linear transformation for the y coordinate, that is independent of the x coordinate, this
     * map has the form `Ty(v) = v * m + b`, where `m` is the slope and `b` is the offset in `v = 0`.
     *
     * The positions of the corners require that `Ty(maxY) = 0` and `Ty(minY) = maxY - minY`, thus it follows that
     * `Ty(v) = v * (-1) + maxX`.
     */
    private fun transformationY(value: Double): Double {
        return value
    }
}