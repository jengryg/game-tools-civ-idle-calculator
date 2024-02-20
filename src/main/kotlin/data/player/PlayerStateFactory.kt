package data.player

import Logging
import common.BuildingType
import common.ResourceAmount
import data.definitions.GameDefinition
import data.definitions.model.City
import data.definitions.model.Technology
import data.definitions.model.Wonder
import data.player.json.SaveGameJson
import data.player.json.TileBuildingJson
import data.player.model.*
import hexagons.Point
import logger
import utils.FileIo
import utils.JsonParser
import kotlin.math.roundToLong

class PlayerStateFactory(
    private val gd: GameDefinition
) : Logging {
    private val log = logger()

    private val playerStatePath = "src/main/resources/player"

    private val json = JsonParser.deserialize<SaveGameJson>(
        FileIo.readCompressedFile("$playerStatePath/CivIdle").also {
            // The save game is compressed json.
            // We write the json to the same directory to make it available for inspection to the user.
            FileIo.writeFile("$playerStatePath/CivIdle.json", it)
        }
    )

    /**
     * [City] of current run.
     */
    private val city: City = gd.cities[json.current.city]!!

    /**
     * [ActiveGreatPerson] in the current run.
     */
    private val greatPerson: Map<String, ActiveGreatPerson> = gd.persons.values.associate {
        it.name to ActiveGreatPerson(person = it)
    }.also { greatPersonMap ->
        json.current.greatPeople.forEach { (pName, level) ->
            // Add current run great People levels.
            greatPersonMap[pName]?.let { it.level += level }
        }
        json.options.greatPeople.forEach { (pName, data) ->
            // Add permanent great People levels.
            greatPersonMap[pName]?.let { it.level = data.level }
        }
    }

    /**
     * [Technology] unlocked in the current run.
     */
    private val unlockedTechnology: Map<String, Technology> = json.current.unlockedTech.mapValues {
        gd.technologies[it.key]!!
    }

    /**
     * [Tiles] representing the map in the current run.
     */
    private val tiles = json.current.tiles.value.associate { mts ->
        Point.fromTileInteger(mts.tile).let { xy ->
            mts.tile to MapTile(
                id = mts.id,
                tile = mts.tile,
                explored = mts.explored,
                deposit = mts.deposit.associate { dName ->
                    dName to gd.deposits[dName]!!
                },
                building = mts.building?.let { createBuildBuilding(it) },
                point = xy,
                pointSvg = city.grid.layout.hexToPixel(city.grid.gridToHex(xy))
            )
        }
    }

    private fun createBuildBuilding(building: TileBuildingJson) = BuildBuilding(
        building = gd.buildings[building.type]!!,
        level = building.level,
        desiredLevel = building.desiredLevel,
        capacity = building.capacity,
        stockpileCapacity = building.stockpileCapacity,
        stockpileMax = building.stockpileMax,
        priority = building.priority,
        options = building.options,
        electrification = building.electrification,
        status = when (building.status) {
            "building" -> BuildingStatus.BUILDING
            "upgrading" -> BuildingStatus.UPGRADING
            "paused" -> BuildingStatus.PAUSED
            "completed" -> BuildingStatus.COMPLETED
            else -> throw IllegalArgumentException("Building Status Unknown: ${building.status}.")
        },
        resources = building.resources.map { (rName, rAmount) ->
            ResourceAmount(
                resource = gd.resources[rName]!!,
                amount = rAmount.roundToLong()
            )
        },
    )

    /**
     * [Wonder] build or discovered on the current map.
     */
    private val activeWonders: Map<String, Wonder> = json.current.tiles.value.filter { it.explored }.mapNotNull { mts ->
        mts.building?.let { gd.buildings[it.type] }?.let { bld ->
            if (bld.special == BuildingType.WORLD_WONDER || bld.special == BuildingType.NATURAL_WONDER) {
                bld.name to Wonder(
                    name = bld.name,
                    building = bld,
                    stdBoost = gd.wonders[bld.name]!!.stdBoost
                )
            } else {
                null
            }
        }
    }.toMap()

    fun getPlayState(): PlayerState {
        return PlayerState(
            city = city,
            greatPeople = greatPerson,
            unlockedTechnology = unlockedTechnology,
            tiles = tiles,
            activeWonders = activeWonders,
        )
    }
}