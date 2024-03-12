package analysis.processors.citymap

import data.definitions.model.Building
import data.definitions.model.Deposit
import utils.DVIPSColors
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
            else -> DVIPSColors.Green
        }
    }
}