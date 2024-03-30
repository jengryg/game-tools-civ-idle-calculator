package analysis.visual

import game.common.BuildingType
import game.model.game.Building
import game.model.game.Deposit
import utils.svg.DVIPSColors
import java.awt.Color

object ColorSchema {
    fun getColor(deposit: Deposit): Color {
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

    fun getColor(building: Building): Color {
        return when (building.name) {
            "GrottaAzzurra" -> DVIPSColors.Magenta
            "MountSinai" -> DVIPSColors.Magenta
            else -> getBuildingColorByType(building)
        }
    }

    private fun getBuildingColorByType(building: Building): Color {
        return when (building.type) {
            BuildingType.NORMAL -> DVIPSColors.SpringGreen
            BuildingType.HQ -> DVIPSColors.NavyBlue
            BuildingType.WORLD_WONDER -> DVIPSColors.BurntOrange
            BuildingType.NATURAL_WONDER -> DVIPSColors.Green
        }
    }
}