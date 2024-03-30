package game.loader.player.data

import game.loader.game.data.ResourceData
import utils.io.HasIdBase

class TransportationData(
    id: Int,
    val from: TileData,
    val to: TileData,
    val fromPosition: Pair<Double, Double>,
    val toPosition: Pair<Double, Double>,
    val ticksRequired: Int,
    val ticksSpent: Int,
    val hasEnoughFuel: Boolean,
    val fuelAmount: Pair<ResourceData, Double>,
    val currentFuelAmount: Pair<ResourceData, Double>,
    val resourceAmount: Pair<ResourceData, Double>
) : HasIdBase(id)