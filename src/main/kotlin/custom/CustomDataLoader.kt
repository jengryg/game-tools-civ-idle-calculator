package custom

import Logging
import data.model.player.ActiveGreatPerson
import data.model.player.BuildBuilding
import data.model.player.BuildingStatus
import data.model.player.MapTile
import data.json.player.SaveGameJson
import data.json.player.TileBuildingJson
import data.GameDefinition
import common.BuildingType
import data.model.definitions.City
import common.ResourceAmount
import data.PlayerState
import data.model.definitions.Wonder
import game.hex.Point
import logger
import utils.FileIo
import utils.JsonParser
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.math.roundToLong


class CustomDataLoader(
    private val gameDefinitions: GameDefinition
) : Logging {
    private val log = logger()

    private val customDataFile: String = "src/main/resources/player/CivIdle"

    private val cor: PlayerState

    init {
        FileIo.readCompressedFile(customDataFile).let {
            Files.writeString(
                Paths.get("$customDataFile.json"),
                it,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }

        JsonParser.deserialize<SaveGameJson>(FileIo.readFile("$customDataFile.json")).let { save ->
            val city = gameDefinitions.cities[save.current.city]!!
            cor = PlayerState(
                city = city,
                greatPeople = extractGreatPeople(save),
                unlockedTechnologies = extractUnlockedTechnologies(save),
                tiles = extractTiles(save, city),
                activeWonders = extractActiveWonders(save)
            )
        }
    }

    private fun extractActiveWonders(save: SaveGameJson) =
        save.current.tiles.value.filter { it.explored }.mapNotNull { mapTile ->
            if (mapTile.building == null) {
                null
            } else {
                val building = gameDefinitions.buildings[mapTile.building.type]!!

                if (building.special == BuildingType.WORLD_WONDER || building.special == BuildingType.NATURAL_WONDER) {
                    building.name to Wonder(
                        name = building.name,
                        building = building,
                        stdBoost = gameDefinitions.wonders[building.name]!!.stdBoost
                    )
                } else {
                    null
                }
            }
        }.toMap()

    private fun extractTiles(
        save: SaveGameJson,
        city: City
    ) = save.current.tiles.value.associate { json ->
        val tileXY = Point.fromTileInteger(json.tile)

        json.tile to MapTile(
            id = json.id,
            tile = json.tile,
            explored = json.explored,
            deposit = json.deposit.map { dName ->
                dName to gameDefinitions.deposits[dName]!!
            }.toMap(),
            building = json.building?.let { building ->
                createBuildBuilding(building)
            },
            point = tileXY,
            pointSvg = city.grid.layout.hexToPixel(city.grid.gridToHex(tileXY))
        )
    }

    private fun createBuildBuilding(building: TileBuildingJson) = BuildBuilding(
        building = gameDefinitions.buildings[building.type]!!,
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
                resource = gameDefinitions.resources[rName]!!,
                amount = rAmount.roundToLong()
            )
        },
    )

    private fun extractUnlockedTechnologies(save: SaveGameJson) = save.current.unlockedTech.map { (key, _) ->
        key to gameDefinitions.technologies[key]!!
    }.toMap()

    private fun extractGreatPeople(save: SaveGameJson) = gameDefinitions.persons.values.map {
        it.name to ActiveGreatPerson(person = it)
    }.toMap().also { gps ->
        save.current.greatPeople.forEach { (pName, level) ->
            gps[pName]?.let {
                it.level += level
            }
        }
        save.options.greatPeople.forEach { (pName, data) ->
            gps[pName]?.let {
                it.level = data.level
            }
        }
    }.toMap()

    fun getRegistry(): PlayerState {
        return cor
    }
}