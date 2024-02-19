package custom

import Logging
import SVG_HEXAGON_SIZE
import data.GameDefinition
import common.BuildingType
import data.PlayerState
import data.model.definitions.Deposit
import game.hex.Point
import logger
import utils.DVIPSColors
import utils.DVIPSColors.withAlpha
import utils.SVGImage
import java.awt.Color
import kotlin.math.sqrt

class SvgExporter(
    gameDefinitions: GameDefinition,
    playerState: PlayerState
) : Logging {
    private val log = logger()

    val city = playerState.city
    val tiles = playerState.tiles

    private val svg = playerState.city.grid.hexagons.flatten().let { p ->
        SVGImage(
            coordinateMaxX = p.maxOf { it.x } + sqrt(3.0) * SVG_HEXAGON_SIZE / 2.0,
            coordinateMaxY = p.maxOf { it.y } + sqrt(3.0) * SVG_HEXAGON_SIZE / 2.0,
        )
    }

    private fun getColor(deposit: Deposit): Color {
        return when (deposit.name) {
            "Water" -> DVIPSColors.NavyBlue
            "Copper" -> DVIPSColors.Orange
            "Iron" -> DVIPSColors.Red
            "Wood" -> DVIPSColors.Brown
            "Stone" -> DVIPSColors.Gray
            "Gold" -> DVIPSColors.Goldenrod
            "Coal" -> DVIPSColors.Black
            "Oil" -> DVIPSColors.BlueGreen
            "Aluminum" -> DVIPSColors.GreenYellow
            "NaturalGas" -> DVIPSColors.Violet
            else -> return DVIPSColors.Transparent
        }
    }

    fun drawHexagons() {
        city.grid.pointIteration.map { p ->
            val polygon = city.grid.layout.polygonCorners(
                city.grid.gridToHex(p)
            )

            log.atDebug()
                .setMessage("Drawing Hexagon.")
                .addKeyValue("xy") { p }
                .addKeyValue("hexagon") { polygon }
                .log()

            val tile = tiles[Point.toTileInteger(p)]!!

            if (tile.deposit.isNotEmpty()) {
                tile.deposit.values.toList().forEachIndexed { index, deposit ->
                    svg.color(getColor(deposit).withAlpha(128))
                    svg.polygon(polygon.slice(index .. (index + 1)).plus(tile.pointSvg))
                }
            }

            when (tile.building?.building?.special) {
                BuildingType.NORMAL -> {}
                BuildingType.HQ -> {
                    svg.color(DVIPSColors.Blue)
                    svg.polygon(polygon, true)
                }

                BuildingType.WORLD_WONDER -> {
                    svg.color(DVIPSColors.BurntOrange)
                    svg.polygon(polygon, true)
                }

                BuildingType.NATURAL_WONDER -> {
                    svg.color(DVIPSColors.Green)
                    svg.polygon(polygon, true)
                }

                null -> {}
            }

            if (!tile.explored) {
                svg.color(DVIPSColors.Gray.withAlpha(64))
                svg.polygon(polygon, true)
            }

            if(tile.deposit.isNotEmpty()) {
                svg.color(DVIPSColors.Black)
                svg.fontSize(10)
                svg.write(
                    tile.deposit.keys.joinToString(" "),
                    tile.pointSvg.x - SVG_HEXAGON_SIZE * 0.80,
                    tile.pointSvg.y - SVG_HEXAGON_SIZE / 3
                )
            }

            if(tile.building?.building?.tier == 1 && tile.building?.building?.special == BuildingType.NORMAL) {
                svg.color(DVIPSColors.Black)
                svg.circle(tile.pointSvg, 10.0, true)
            }

            svg.color(DVIPSColors.Black)
            svg.polygon(polygon, filled = false)
        }
    }

    fun drawBuildings() {
        city.grid.pointIteration.map { p ->
            val tile = tiles[Point.toTileInteger(p)]!!


            svg.fontSize(10)

            if (tile.building != null) {
                log.atDebug()
                    .setMessage("Drawing Buildings.")
                    .addKeyValue("xy") { p }
                    .addKeyValue("building") { tile.building.building.name }
                    .log()

                svg.write(
                    "${tile.building.building.name} ${tile.building.level}",
                    tile.pointSvg.x - SVG_HEXAGON_SIZE * 0.8,
                    tile.pointSvg.y + SVG_HEXAGON_SIZE / 3
                )
            }
        }
    }

    fun export() {
        svg.export("SaveGame_${city.name}")
    }


}