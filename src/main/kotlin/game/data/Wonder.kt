package game.data

import kotlin.math.floor
import kotlin.math.roundToInt

class Wonder(
    val name: String,
    val building: Building,
    val stdBoost: List<StandardBoost>?,
) {
    fun alpsBoost(level: Int): Int {
        return if (building.name == ALPS) {
            floor(level.toDouble() / 10.0).roundToInt()
        } else {
            0
        }
    }

    companion object {
        const val ALPS = "Alps"
    }
}