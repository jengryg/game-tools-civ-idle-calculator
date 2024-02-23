package analysis.processors.chain

import CHAIN_TEXT_LINE_START
import data.definitions.model.Resource
import utils.nf

class ChainNodeIO(
    val resource: Resource,
    val rawAmount: Long,
    val effects: Map<String, Double>,
    val wonders: Map<String, Double>,
    val technologies: Map<String, Double>,
    val alpMulti: Double,
) {
    val effectiveAmount = rawAmount * effectiveMultiplier()

    private fun effectiveMultiplier(): Double {
        return 1.0 + effects.values.sumOf { it } + wonders.values.sumOf { it } + technologies.values.sumOf { it } + alpMulti
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
        val technologyBlock =
            technologies.mapNotNull { "${it.key} ${it.value}".takeIf { s -> s.isNotBlank() && it.value != 0.0 } }
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

        if (alpMulti != 0.0) result.add("Alps $alpMulti")
        if (effectBlock != null) result.add("$effectBlock")
        if (wonderBlock != null) result.add("$wonderBlock")
        if (technologyBlock != null) result.add("$technologyBlock")

        return result.joinToString(" ")
    }
}