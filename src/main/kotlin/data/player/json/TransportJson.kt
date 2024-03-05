 package data.player.json

class TransportJson(
    val id: Int,
    val fromXy: Int,
    val toXy: Int,
    val fromPosition: Pair<Double, Double>,
    val toPosition: Pair<Double, Double>,
    val ticksRequired: Int,
    val ticksSpent: Int,
    val resource: String,
    val amount: Double,
    val fuel: String,
    val fuelAmount: Double,
    val currentFuelAmount: Double,
    val hasEnoughFuel: Boolean
)