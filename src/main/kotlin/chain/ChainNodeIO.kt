package chain

import game.data.Resource
import utils.nf

class ChainNodeIO(
    val resource: Resource,
    val rawAmount: Long,
    val effects: Map<String, Int>
) {
    val effectiveAmount = rawAmount * effectiveMultiplier()

    private fun effectiveMultiplier(): Int {
        return 1 + effects.values.sumOf { it }
    }

    override fun toString(): String {
        return "${resource.name} (${nf(rawAmount)}): ${nf(effectiveAmount)}"
    }
}