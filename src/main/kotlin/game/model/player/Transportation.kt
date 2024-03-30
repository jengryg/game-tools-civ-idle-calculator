package game.model.player

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.model.game.Resource
import utils.io.HasIdBase
import utils.io.JustIdSerializer

class Transportation(
    id: Int,
    @JsonSerialize(using = JustIdSerializer::class)
    val from: Tile,
    @JsonSerialize(using = JustIdSerializer::class)
    val to: Tile,
    val fromPosition: Pair<Double, Double>,
    val toPosition: Pair<Double, Double>,
    val ticksRequired: Int,
    val ticksSpent: Int,
    val hasEnoughFuel: Boolean,
    val fuelAmount: Pair<Resource, Double>,
    val currentFuelAmount: Pair<Resource, Double>,
    val resourceAmount: Pair<Resource, Double>
) : HasIdBase(id)