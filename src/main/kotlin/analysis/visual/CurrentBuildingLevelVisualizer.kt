package analysis.visual

import constants.SVG_HEXAGON_SIZE
import game.model.Model
import logger

class CurrentBuildingLevelVisualizer(
    private val model: Model
) : BaseHexagonalMap(mapSize = model.currentCity.size, tiles = model.tiles), Logging {
    private val log = logger()

    private val xOffset = -SVG_HEXAGON_SIZE * 0.8
    private val yOffset = SVG_HEXAGON_SIZE / 3.0

    fun visualize() {
        addBuildings()
        drawHexagonBoundaries()
        flushTextBuffer()
    }

    fun addBuildings() {
        tileIteration.forEach { tile ->
            val bld = tile.building?.bld ?: return@forEach

            hexagonCircle(
                color = VisualizerSchema.getColor(tile.building.level),
                tl = tile,
                scale = 1.0
            )

            writeToTextBuffer(
                string = "${bld.name} ${tile.building.level}",
                hexagon = hexagonToPixel(tile.point),
                xOffset = xOffset,
                yOffset = yOffset
            )
        }

        log.atDebug()
            .setMessage("Drawn building levels on map.")
            .log()
    }

    fun export(suffix: String = "") {
        svg.export("Visualize_levels_${model.currentCity.name}${suffix}")
    }
}