package game.loader.game

import Logging
import game.loader.game.json.*
import logger
import utils.io.FileIo
import utils.io.JsonParser

/**
 * Loads the game definitions from the json files located at [GameJsonLoaderConfiguration.gameInputDirectory].
 */
class GameJsonLoader(
    configuration: GameJsonLoaderConfiguration? = null
) : Logging {
    private val log = logger()

    private val cfg = configuration ?: GameJsonLoaderConfiguration().also {
        log.atInfo()
            .setMessage("Initialized ${this::class.simpleName} with default configuration.")
            .log()
    }

    fun load(): GameJson {
        val ages = loadAges()
        val buildings = loadBuildings()
        val cities = loadCities()
        val deposits = loadDeposits()
        val greatPeople = loadGreatPeople()

        val resourcesWithoutPrice = loadResourcesWithoutPrice()
        val resourcesWithoutStorage = loadResourcesWithoutStorage()

        val resources = loadResources().onEach { (name, rj) ->
            rj.canPrice = resourcesWithoutPrice[name] != true
            rj.canStore = resourcesWithoutStorage[name] != true
        }

        val technologies = loadTechnologies()

        val wonders = loadWonders()

        log.atInfo()
            .setMessage("Completed loading game data.")
            .addKeyValue("directory") { cfg.gameInputDirectory }
            .log()

        return GameJson(
            ages = ages,
            buildings = buildings,
            cities = cities,
            deposits = deposits,
            greatPeople = greatPeople,
            resources = resources,
            technologies = technologies,
            wonders = wonders
        )
    }

    private fun loadAges(): Map<String, AgeJson> =
        JsonParser.deserialize<Map<String, AgeJson>>(FileIo.readFile("${cfg.gameInputDirectory}/ages.json"))

    private fun loadBuildings(): Map<String, BuildingJson> =
        JsonParser.deserialize<Map<String, BuildingJson>>(FileIo.readFile("${cfg.gameInputDirectory}/buildings.json"))

    private fun loadCities(): Map<String, CityJson> =
        JsonParser.deserialize<Map<String, CityJson>>(FileIo.readFile("${cfg.gameInputDirectory}/cities.json"))

    private fun loadDeposits(): Map<String, Boolean> =
        JsonParser.deserialize<Map<String, Boolean>>(FileIo.readFile("${cfg.gameInputDirectory}/deposits.json"))

    private fun loadGreatPeople(): Map<String, GreatPersonJson> =
        JsonParser.deserialize<Map<String, GreatPersonJson>>(FileIo.readFile("${cfg.gameInputDirectory}/persons.json"))

    private fun loadResources(): Map<String, ResourceJson> =
        JsonParser.deserialize<Map<String, ResourceJson>>(FileIo.readFile("${cfg.gameInputDirectory}/resources.json"))

    private fun loadResourcesWithoutPrice(): Map<String, Boolean> =
        JsonParser.deserialize<Map<String, Boolean>>(FileIo.readFile("${cfg.gameInputDirectory}/resources-noPrice.json"))

    private fun loadResourcesWithoutStorage(): Map<String, Boolean> =
        JsonParser.deserialize<Map<String, Boolean>>(FileIo.readFile("${cfg.gameInputDirectory}/resources-noStorage.json"))

    private fun loadTechnologies(): Map<String, TechJson> =
        JsonParser.deserialize<Map<String, TechJson>>(FileIo.readFile("${cfg.gameInputDirectory}/techs.json"))

    private fun loadWonders(): Map<String, List<WonderJson>> =
        JsonParser.deserialize<Map<String, List<WonderJson>>>(FileIo.readFile("${cfg.gameInputDirectory}/wonders.json"))
}