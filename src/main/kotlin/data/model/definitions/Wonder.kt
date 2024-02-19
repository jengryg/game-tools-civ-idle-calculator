package data.model.definitions

import common.StandardBoost
import kotlin.math.floor
import kotlin.math.roundToInt

class Wonder(
    val name: String,
    val building: Building,
    val stdBoost: List<StandardBoost>?,
) {
    /**
     * Alps apply to all normal buildings based in the buildings level.
     * For every completed 10 levels of the building, they add 1 Output and 1 Input multiplier.
     *
     * Note: Alps reduce the efficiency of buildings, i.e. more input required per output.
     */
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