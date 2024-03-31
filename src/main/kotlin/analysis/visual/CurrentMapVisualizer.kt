package analysis.visual

import Logging
import SVG_HEXAGON_SIZE
import game.common.BuildingType
import game.model.Model
import logger
import utils.svg.DVIPSColors
import utils.svg.DVIPSColors.withAlpha

class CurrentMapVisualizer(
    private val model: Model
) : BaseHexagonalMap(mapSize = model.currentCity.size, tiles = model.tiles), Logging {
    private val log = logger()

    fun visualize() {
        addDeposits()
        addBuildings()

        addFogOfWar(DVIPSColors.Gray.withAlpha(128))
        drawHexagonBoundaries()
        flushTextBuffer()
    }

    private fun addDeposits() {
        tileIteration.forEach { tile ->
            if (tile.deposit.isNotEmpty()) {
                tile.deposit.values.toList().forEachIndexed { index, deposit ->
                    drawHexagonSlice(
                        color = VisualizerSchema.getColor(deposit),
                        tile,
                        index..index + 1
                    )
                }
                writeToTextBuffer(
                    string = tile.deposit.keys.joinToString(" "),
                    hexagon = hexagonToPixel(tile.point),
                    xOffset = -SVG_HEXAGON_SIZE * 0.80,
                    yOffset = -SVG_HEXAGON_SIZE / 3.0
                )
            }
        }

        log.atDebug()
            .setMessage("Drawn deposits on map.")
            .log()
    }

    private fun addBuildings() {
        val xOffset = -SVG_HEXAGON_SIZE * 0.8
        val yOffset = SVG_HEXAGON_SIZE / 3.0

        tileIteration.forEach { tile ->
            val bld = tile.building?.bld ?: return@forEach

            hexagonCircle(
                color = VisualizerSchema.getColor(bld),
                tl = tile,
                scale = VisualizerSchema.getBuildingCircleRadius(bld)
            )

            when (bld.type) {
                BuildingType.NORMAL -> {
                    writeToTextBuffer(
                        string = "${bld.name} ${tile.building.level}",
                        hexagon = hexagonToPixel(tile.point),
                        xOffset = xOffset,
                        yOffset = yOffset
                    )
                }

                BuildingType.HQ -> {
                    writeToTextBuffer(
                        string = "${bld.name} CENTER",
                        hexagon = hexagonToPixel(tile.point),
                        xOffset = xOffset,
                        yOffset = yOffset
                    )
                }

                BuildingType.WORLD_WONDER -> {
                    writeToTextBuffer(
                        string = "${bld.name} WRD WND",
                        hexagon = hexagonToPixel(tile.point),
                        xOffset = xOffset,
                        yOffset = yOffset
                    )
                }

                BuildingType.NATURAL_WONDER -> {
                    writeToTextBuffer(
                        string = "${bld.name} NAT WND",
                        hexagon = hexagonToPixel(tile.point),
                        xOffset = xOffset,
                        yOffset = yOffset
                    )
                }
            }
        }

        log.atDebug()
            .setMessage("Drawn buildings on map.")
            .log()
    }

    fun export() {
        svg.export("Visualize_${model.currentCity.name}")
    }
}