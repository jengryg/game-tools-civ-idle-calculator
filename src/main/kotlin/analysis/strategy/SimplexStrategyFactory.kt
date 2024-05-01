package analysis.strategy

import game.model.Model
import game.model.game.Resource
import kotlin.math.ceil

class SimplexStrategyFactory(
    private val model: Model,
) {
    fun process(name: String, amount: Double): SimplexAlgorithm {
        return SimplexAlgorithm(
            name = name,
            model = model,
            target = BuildingVector.just(model.getBuilding(name), amount),
            happyness = BuildingVector(model.buildings.values.toList(), 1.0),
            ceilingScale = 1.0
        )
    }

    fun processByEnterpriseValueTarget(name: String, value: Double): SimplexAlgorithm {
        val building = model.getBuilding(name)
        val r = Resource.getEvOf(building.effectiveOutput)
        require(r > 0.0) { "Can not target EV production value for building that does not output EV: $name" }

        return process(name, ceil(value / r))
    }
}