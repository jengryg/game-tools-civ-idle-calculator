package analysis.processors.citymap

import Logging
import SVG_HEXAGON_SIZE
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import common.BuildingType
import hexagons.Point
import logger
import utils.DVIPSColors

class CurrentMapProcessor(
    ap: AnalyserProvider
) : BaseHexagonalMap(city = ap.psa.city, tiles = ap.psa.tiles), IAnalyserProvider by ap, Logging {
    private val log = logger()

    fun createMap() {
        addDeposits()
        addBuildings()

        addFogOfWar()
        drawHexagonBoundaries()
        flushTextBuffer()
    }

    fun addDeposits() {
        city.grid.pointIteration.map { p ->
            val tile = tiles[Point.toTileInteger(p)]!!

            if (tile.deposit.isNotEmpty()) {
                tile.deposit.values.toList().forEachIndexed { index, deposit ->
                    drawHexagonSlice(
                        color = ColorSchema.getColor(deposit),
                        tile,
                        index..index + 1
                    )
                }
                writeToTextBuffer(
                    string = tile.deposit.keys.joinToString(" "),
                    hexagon = tile.pointSvg,
                    xOffset = -SVG_HEXAGON_SIZE * 0.80,
                    yOffset = -SVG_HEXAGON_SIZE / 3.0
                )
            }
        }
    }

    fun addBuildings() {
        city.grid.pointIteration.map { p ->
            val tile = tiles[Point.toTileInteger(p)]!!

            val xOffset = -SVG_HEXAGON_SIZE * 0.8
            val yOffset = SVG_HEXAGON_SIZE / 3.0

            when (tile.building?.building?.special) {
                BuildingType.NORMAL -> {
                    hexagonCircle(color = DVIPSColors.SpringGreen, mapTile = tile)
                    writeToTextBuffer(
                        string = "${tile.building.building.name} ${tile.building.level}",
                        hexagon = tile.pointSvg,
                        xOffset = xOffset,
                        yOffset = yOffset
                    )
                }

                BuildingType.HQ -> {
                    hexagonCircle(color = DVIPSColors.NavyBlue, mapTile = tile)
                    writeToTextBuffer(
                        string = "${tile.building.building.name} CENTER",
                        hexagon = tile.pointSvg,
                        xOffset = xOffset,
                        yOffset = yOffset
                    )
                }

                BuildingType.WORLD_WONDER -> {
                    hexagonCircle(color = DVIPSColors.BurntOrange, mapTile = tile)
                    writeToTextBuffer(
                        string = "${tile.building.building.name} WRD WND",
                        hexagon = tile.pointSvg,
                        xOffset = xOffset,
                        yOffset = yOffset
                    )
                }

                BuildingType.NATURAL_WONDER -> {
                    if (tile.building.building.name == "GrottaAzzurra") {
                        hexagonCircle(color = DVIPSColors.Magenta, mapTile = tile, scale = 0.75)
                    } else {
                        hexagonCircle(color = DVIPSColors.Green, mapTile = tile, scale = 0.75)
                    }

                    writeToTextBuffer(
                        string = "${tile.building.building.name} NAT WND",
                        hexagon = tile.pointSvg,
                        xOffset = xOffset,
                        yOffset = yOffset
                    )
                }

                null -> {}
            }
        }
    }

    fun export() {
        svg.export("SaveGame_${city.name}_BaseMap")
    }


}