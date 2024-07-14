package analysis.visual

import constants.*
import game.common.BuildingType
import game.model.game.Building
import game.model.game.Deposit
import utils.svg.DVIPSColors
import java.awt.Color

object VisualizerSchema {
    fun getColor(deposit: Deposit): Color {
        return when (deposit.name) {
            WATER -> DVIPSColors.NavyBlue
            COPPER -> DVIPSColors.Orange
            IRON -> DVIPSColors.Red
            WOOD -> DVIPSColors.Brown
            STONE -> DVIPSColors.Gray
            GOLD -> DVIPSColors.Goldenrod
            COAL -> DVIPSColors.CadetBlue
            OIL -> DVIPSColors.Rhodamine
            ALUMINUM -> DVIPSColors.GreenYellow
            NATURAL_GAS -> DVIPSColors.Violet
            URANIUM -> DVIPSColors.OliveGreen
            else -> return DVIPSColors.Transparent
        }
    }

    fun getColor(building: Building): Color {
        return when (building.name) {
            GROTTA_AZZURRA -> DVIPSColors.Magenta
            MOUNT_SINAI -> DVIPSColors.Magenta
            MOUNT_TAI -> DVIPSColors.Magenta
            else -> getBuildingColorByType(building)
        }
    }

    fun getColor(number: Int): Color {
        return DVIPSColors.palette[number % DVIPSColors.palette.size]
    }

    private fun getBuildingColorByType(building: Building): Color {
        return when (building.type) {
            BuildingType.NORMAL -> DVIPSColors.SpringGreen
            BuildingType.HQ -> DVIPSColors.NavyBlue
            BuildingType.WORLD_WONDER -> DVIPSColors.BurntOrange
            BuildingType.NATURAL_WONDER -> DVIPSColors.Green
        }
    }

    fun getBuildingCircleRadius(building: Building): Double {
        return when (building.name) {
            else -> getBuildingCircleRadiusByType(building)
        }
    }

    private fun getBuildingCircleRadiusByType(building: Building): Double {
        return when (building.type) {
            BuildingType.NORMAL -> 0.25
            BuildingType.HQ -> 0.5
            BuildingType.WORLD_WONDER -> 0.25
            BuildingType.NATURAL_WONDER -> 0.7
        }
    }
}