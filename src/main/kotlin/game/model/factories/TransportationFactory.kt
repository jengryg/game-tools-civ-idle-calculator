package game.model.factories

import game.loader.player.data.TransportationData
import game.model.game.Resource
import game.model.player.Tile
import game.model.player.Transportation

class TransportationFactory(
    private val resources: Map<String, Resource>,
    private val tiles: Map<Int, Tile>
) {
    fun process(transportations: Map<Int, TransportationData>): Map<Int, Transportation> {
        return transportations.mapValues { (_, data) -> create(data) }
    }

    private fun create(data: TransportationData): Transportation {
        require(data.fuelAmount.first.name == data.currentFuelAmount.first.name)

        val fuel = resources[data.fuelAmount.first.name]!!
        val res = resources[data.resourceAmount.first.name]!!

        return Transportation(
            id = data.id,
            from = tiles[data.from.id]!!,
            to = tiles[data.to.id]!!,
            fromPosition = data.fromPosition,
            toPosition = data.toPosition,
            ticksRequired = data.ticksRequired,
            ticksSpent = data.ticksSpent,
            hasEnoughFuel = data.hasEnoughFuel,
            fuelAmount = Pair(fuel, data.fuelAmount.second),
            currentFuelAmount = Pair(fuel, data.currentFuelAmount.second),
            resourceAmount = Pair(res, data.resourceAmount.second)
        )
    }
}