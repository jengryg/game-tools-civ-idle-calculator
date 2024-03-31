package game.model

import Logging
import game.loader.game.GameData
import game.loader.player.PlayerData
import game.model.factories.*
import game.model.game.*
import game.model.player.Tile
import logger
import utils.io.FileIo
import utils.io.JsonParser

class ModelFactory(
    configuration: ModelFactoryConfiguration? = null
) : Logging {
    private val log = logger()

    private val cfg = configuration ?: ModelFactoryConfiguration().also {
        log.atInfo()
            .setMessage("Initialized ${this::class.simpleName} with default configuration.")
            .log()
    }

    fun create(gd: GameData, pd: PlayerData): Model {
        val ages = processAges(gd)

        val technologies = processTechnologies(gd, ages)
        val greatPersons = processGreatPersons(gd, ages)

        val deposits = processDeposits(gd, technologies)

        val cities = processCities(gd, deposits)
        val resources = processResources(gd, deposits, technologies)

        val buildings = processBuildings(gd, resources, technologies, deposits)

        val obtainedGreatPeoples = processObtainedGreatPeoples(pd, greatPersons)

        val tiles = processTiles(pd, resources, buildings, deposits)

        val transports = processTransports(pd, resources, tiles)

        return Model(
            ages = ages,
            buildings = buildings,
            cities = cities,
            deposits = deposits,
            greatPersons = greatPersons,
            resources = resources,
            technologies = technologies,
            obtainedGreatPeoples = obtainedGreatPeoples,
            tiles = tiles,
            transports = transports,
            currentCity = cities[pd.city.name]!!,
            unlockedTechnologies = pd.unlockedTechnology.mapValues { technologies[it.key]!! }
        ).also {
            FileIo.writeFile(cfg.output, JsonParser.serialize(it))
        }
    }

    private fun processAges(gd: GameData): Map<String, Age> =
        AgeFactory().process(gd.ages)
            .also {
                log.atInfo()
                    .setMessage("Factory created ages.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processTechnologies(gd: GameData, ages: Map<String, Age>): Map<String, Technology> =
        TechnologyFactory(ages).process(gd.technologies)
            .also {
                log.atInfo()
                    .setMessage("Factory created technologies.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processGreatPersons(gd: GameData, ages: Map<String, Age>): Map<String, GreatPerson> =
        GreatPersonFactory(ages).process(gd.greatPersons)
            .also {
                log.atInfo()
                    .setMessage("Factory created great persons.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processDeposits(gd: GameData, technologies: Map<String, Technology>): Map<String, Deposit> =
        DepositFactory(technologies).process(gd.deposits)
            .also {
                log.atInfo()
                    .setMessage("Factory created deposits.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processCities(gd: GameData, deposits: Map<String, Deposit>) =
        CityFactory(deposits).process(gd.cities)
            .also {
                log.atInfo()
                    .setMessage("Factory created cities.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processResources(gd: GameData, deposits: Map<String, Deposit>, technologies: Map<String, Technology>) =
        ResourceFactory(deposits, technologies).process(gd.resources)
            .also {
                log.atInfo()
                    .setMessage("Factory created resources.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processBuildings(
        gd: GameData,
        resources: Map<String, Resource>,
        technologies: Map<String, Technology>,
        deposits: Map<String, Deposit>
    ) =
        BuildingFactory(resources, technologies, deposits).process(gd.buildings)
            .also {
                log.atInfo()
                    .setMessage("Factory created buildings.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processObtainedGreatPeoples(pd: PlayerData, greatPersons: Map<String, GreatPerson>) =
        ObtainedGreatPeopleFactory(greatPersons).process(pd.obtainedGreatPeople)
            .also {
                log.atInfo()
                    .setMessage("Factory created obtained great people.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processTiles(
        pd: PlayerData,
        resources: Map<String, Resource>,
        buildings: Map<String, Building>,
        deposits: Map<String, Deposit>
    ) =
        TileFactory(resources, buildings, deposits).process(pd.tiles).also {
            log.atInfo()
                .setMessage("Factory created tiles.")
                .addKeyValue("size") { it.size }
                .log()
        }

    private fun processTransports(
        pd: PlayerData,
        resources: Map<String, Resource>,
        tiles: Map<Int, Tile>
    ) =
        TransportationFactory(resources, tiles).process(pd.transportation).also {
            log.atInfo()
                .setMessage("Factory created transports.")
                .addKeyValue("size") { it.size }
                .log()
        }
}
