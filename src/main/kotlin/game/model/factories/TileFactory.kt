package game.model.factories

import game.loader.player.data.TileBuildingData
import game.loader.player.data.TileData
import game.model.game.Building
import game.model.game.Deposit
import game.model.game.Resource
import game.model.player.Tile
import game.model.player.TileBuilding

class TileFactory(
    private val resources: Map<String, Resource>,
    private val buildings: Map<String, Building>,
    private val deposits: Map<String, Deposit>
) {
    fun process(tiles: Map<Int, TileData>): Map<Int, Tile> {
        return tiles.mapValues { (_, data) -> create(data) }
    }

    private fun create(data: TileData): Tile {
        return Tile(
            id = data.id,
            tile = data.tile,
            explored = data.explored,
            point = data.point,
            deposit = data.deposit.mapValues { (name, _) -> deposits[name]!! },
            building = data.building?.let { createBuilding(it) }
        )
    }

    private fun createBuilding(data: TileBuildingData): TileBuilding {
        return TileBuilding(
            id = data.id,
            bld = buildings[data.bld.name]!!,
            level = data.level,
            desiredLevel = data.desiredLevel,
            capacity = data.capacity,
            stockpileCapacity = data.stockpileCapacity,
            stockpileMax = data.stockpileMax,
            options = data.options,
            electrification = data.electrification,
            status = data.status,
            resources = data.resources.mapKeys { (r, _) -> resources[r.name]!! }
        )
    }
}