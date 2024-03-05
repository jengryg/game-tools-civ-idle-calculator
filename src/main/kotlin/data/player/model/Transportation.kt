package data.player.model

import common.ResourceAmount

class Transportation(
    val id: Int,
    val from: MapTile,
    val to: MapTile,
    val fromPosition: Pair<Double, Double>,
    val toPosition: Pair<Double, Double>,
    val ticksRequired: Int,
    val ticksSpent: Int,
    val resourceAmount: ResourceAmount,
    val fuelAmount: ResourceAmount,
    val currentFuelAmount: ResourceAmount,
    val hasEnoughFuel: Boolean
)