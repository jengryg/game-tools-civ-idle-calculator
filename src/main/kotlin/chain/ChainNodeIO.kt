package chain

import CHAIN_TEXT_LINE_START
import data.model.definitions.Resource
import utils.nf

class ChainNodeIO(
    val resource: Resource,
    val rawAmount: Long,
    val effects: Map<String, Double>,
    val wonders: Map<String, Double>
) {
    val effectiveAmount = rawAmount * effectiveMultiplier()

    private fun effectiveMultiplier(): Double {
        return 1.0 + effects.values.sumOf { it } + wonders.values.sumOf { it }
    }

    fun text(typeChar: Char): String {
        val effectBlock =
            effects.mapNotNull { "${it.key} ${it.value}".takeIf { s -> s.isNotBlank() && it.value != 0.0 } }
                .joinToString(" ")
                .takeIf { it.isNotBlank() }
        val wonderBlock =
            wonders.mapNotNull { "${it.key} ${it.value}".takeIf { s -> s.isNotBlank() && it.value != 0.0 } }
                .joinToString(" ")
                .takeIf { it.isNotBlank() }

        val result = mutableListOf(
            "${
                CHAIN_TEXT_LINE_START.replace(
                    "%type",
                    "$typeChar"
                )
            } ${resource.name} (${rawAmount.nf()}): ${effectiveAmount.nf()}"
        )

        if (effectBlock != null) result.add("$effectBlock")
        if (wonderBlock != null) result.add("$wonderBlock")

        return result.joinToString(" ")
    }
}