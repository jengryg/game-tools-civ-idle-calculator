package game.loader.player.converter

import game.loader.game.GameData
import game.loader.player.data.TileData
import game.loader.player.data.TransportationData
import game.loader.player.json.TransportJson

class TransportationConverter(
    private val gameData: GameData,
    private val tiles: Map<Int, TileData>,
) {
    fun process(transports: List<TransportJson>): Map<Int, TransportationData> {
        return transports.associate { it.id to create(it) }
    }

    fun create(tp: TransportJson): TransportationData {
        val fuel = gameData.resources[tp.fuel]!!
        val res = gameData.resources[tp.resource]!!
        return TransportationData(
            id = tp.id,
            from = tiles[tp.fromXy]!!,
            to = tiles[tp.toXy]!!,
            fromPosition = tp.fromPosition,
            toPosition = tp.toPosition,
            ticksRequired = tp.ticksRequired,
            ticksSpent = tp.ticksSpent,
            hasEnoughFuel = tp.hasEnoughFuel,
            fuelAmount = Pair(fuel, tp.fuelAmount),
            currentFuelAmount = Pair(fuel, tp.fuelAmount),
            resourceAmount = Pair(res, tp.amount)
        )
    }
}