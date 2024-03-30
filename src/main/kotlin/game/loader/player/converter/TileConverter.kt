package game.loader.player.converter

import game.common.BuildingStatus
import game.loader.game.GameData
import game.loader.player.data.TileBuildingData
import game.loader.player.data.TileData
import game.loader.player.json.TileBuildingJson
import game.loader.player.json.TileJson
import utils.svg.Point

class TileConverter(
    private val gameData: GameData,
) {
    fun process(tiles: List<TileJson>): Map<Int, TileData> {
        return tiles.associate { mts ->
            Point(mts.tile).let { xy -> mts.tile to create(mts, xy) }
        }
    }

    private fun create(mts: TileJson, xy: Point): TileData {
        return TileData(
            id = mts.id,
            tile = mts.tile,
            explored = mts.explored,
            deposit = mts.deposit.associateWith { name -> gameData.deposits[name]!! },
            point = xy,
            building = mts.building?.let { createBuilding(mts.id, it) }
        )
    }

    private fun createBuilding(id: Int, json: TileBuildingJson): TileBuildingData {
        return TileBuildingData(
            id = id,
            bld = gameData.buildings[json.type]!!,
            level = json.level,
            desiredLevel = json.desiredLevel,
            capacity = json.capacity,
            stockpileCapacity = json.stockpileCapacity,
            stockpileMax = json.stockpileMax,
            options = json.options,
            electrification = json.electrification,
            status = BuildingStatus.fromString(json.status),
            resources = json.resources.mapKeys { (r, _) -> gameData.resources[r]!! }
        )
    }
}