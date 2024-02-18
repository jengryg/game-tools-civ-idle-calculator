package custom

import Logging
import custom.data.ActiveGreatPerson
import custom.data.BuildBuilding
import custom.data.BuildingStatus
import custom.data.MapTile
import custom.json.SaveGameJson
import game.GameObjectRegistry
import game.data.ResourceAmount
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
    val gameObjectRegistry: GameObjectRegistry
) : Logging {
    private val log = logger()

    private val customDataFile: String = "src/main/resources/custom/CivIdle"

    private val cor: CustomObjectRegistry

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
            val city = gameObjectRegistry.cities[save.current.city]!!
            cor = CustomObjectRegistry(
                city = city,
                greatPeople = gameObjectRegistry.persons.values.map {
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
                }.toMap(),
                unlockedTechnologies = save.current.unlockedTech.map { (key, _) ->
                    key to gameObjectRegistry.technologies[key]!!
                }.toMap(),
                tiles = save.current.tiles.value.associate { json ->
                    val tileXY = Point.fromTileInteger(json.tile)

                    json.tile to MapTile(
                        id = json.id,
                        tile = json.tile,
                        explored = json.explored,
                        deposit = json.deposit.map { dName ->
                            dName to gameObjectRegistry.deposits[dName]!!
                        }.toMap(),
                        building = json.building?.let { building ->
                            BuildBuilding(
                                building = gameObjectRegistry.buildings[building.type]!!,
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
                                        resource = gameObjectRegistry.resources[rName]!!,
                                        amount = rAmount.roundToLong()
                                    )
                                },
                            )
                        },
                        point = tileXY,
                        pointSvg = city.grid.layout.hexToPixel(city.grid.gridToHex(tileXY))
                    )
                }
            )
        }
    }

    fun getRegistry(): CustomObjectRegistry {
        return cor
    }
}