package analyze

import custom.CustomObjectRegistry
import game.GameObjectRegistry
import java.text.DecimalFormat

class Analyzer(
    private val gameObjectRegistry: GameObjectRegistry,
    private val customObjectRegistry: CustomObjectRegistry
) {
    val dec = DecimalFormat("#,###")

    fun calculateCurrentBuildingValue(): Double {
        return customObjectRegistry.tiles.values.sumOf {
            it.building?.totalCost ?: 0.0
        }
    }

    fun calculateCurrentResourceValue(): Double {
        return customObjectRegistry.tiles.values.sumOf {
            it.building?.resources?.sumOf { ra -> if (ra.resource.canPrice) ra.value() else 0.0 } ?: 0.0
        }
    }

    fun analyze(): Map<String, String> {
        val totalBuildingValue = calculateCurrentBuildingValue()
        val totalResourceValue = calculateCurrentResourceValue()

        return mapOf(
            "Total Building Value" to dec.format(totalBuildingValue),
            "Total Resource Value" to dec.format(totalResourceValue),
            "Total Empire Value" to dec.format(totalResourceValue + totalBuildingValue)
        )
    }
}