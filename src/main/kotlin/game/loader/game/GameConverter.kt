package game.loader.game

import Logging
import game.loader.game.converter.*
import game.loader.game.data.*
import game.loader.game.tasks.BuildingCostTask
import game.loader.game.tasks.BuildingTierTask
import game.loader.game.tasks.ProducerDetectorTask
import game.loader.game.tasks.ResourceTierTask
import logger

class GameConverter(
    configuration: GameConverterConfiguration? = null,
) : Logging {
    private val log = logger()

    private val cfg = configuration ?: GameConverterConfiguration().also {
        log.atInfo()
            .setMessage("Initialized ${this::class.simpleName} with default configuration.")
            .log()
    }

    fun convert(gs: GameJson): GameData {
        val ages = processAges(gs)

        val wonders = processWonders(gs)

        val technologies = processTechnologies(gs, ages)
        val greatPersons = processGreatPersons(gs, ages)

        val deposits = processDeposits(gs, technologies)

        val cities = processCities(gs, deposits, technologies)
        val resources = processResources(gs, deposits)

        val buildings = processBuildings(gs, resources, deposits, technologies, greatPersons, wonders, cities)

        return GameData(
            ages = ages,
            deposits = deposits,
            resources = resources,
            technologies = technologies,
            greatPersons = greatPersons,
            buildings = buildings,
            cities = cities
        ).also {
            ResourceTierTask(it).process()
            BuildingTierTask(it).process()
            BuildingCostTask(it).process()
            ProducerDetectorTask(it).process()
        }
    }

    private fun processAges(gs: GameJson): Map<String, AgeData> =
        AgeConverter().process(gs.ages)
            .also {
                log.atInfo()
                    .setMessage("Processed age data.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processWonders(gs: GameJson): Map<String, List<WonderData>> =
        WonderConverter().process(gs.wonders)
            .also {
                log.atInfo()
                    .setMessage("Processed wonder data.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processTechnologies(gs: GameJson, ages: Map<String, AgeData>): Map<String, TechnologyData> =
        TechnologyConverter(ages).process(gs.technologies)
            .also {
                log.atInfo()
                    .setMessage("Processed technology data.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processCities(
        gs: GameJson,
        deposits: Map<String, DepositData>,
        technologies: Map<String, TechnologyData>
    ): Map<String, CityData> =
        CityConverter(deposits, technologies).process(gs.cities)
            .also {
                log.atInfo()
                    .setMessage("Processed city data.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processDeposits(gs: GameJson, technologies: Map<String, TechnologyData>): Map<String, DepositData> =
        DepositConverter(technologies).process(gs.deposits)
            .also {
                log.atInfo()
                    .setMessage("Processed deposit data.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processResources(gs: GameJson, deposits: Map<String, DepositData>): Map<String, ResourceData> =
        ResourcesConverter(deposits).process(gs.resources)
            .also {
                log.atInfo()
                    .setMessage("Processed resource data.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processGreatPersons(gs: GameJson, ages: Map<String, AgeData>): Map<String, GreatPersonData> =
        GreatPersonConverter(ages).process(gs.greatPeople)
            .also {
                log.atInfo()
                    .setMessage("Processed great person data.")
                    .addKeyValue("size") { it.size }
                    .log()
            }

    private fun processBuildings(
        gs: GameJson,
        resources: Map<String, ResourceData>,
        deposits: Map<String, DepositData>,
        technologies: Map<String, TechnologyData>,
        greatPersons: Map<String, GreatPersonData>,
        wonders: Map<String, List<WonderData>>,
        cities: Map<String, CityData>
    ): Map<String, BuildingData> =
        BuildingConverter(resources, deposits, technologies, greatPersons, wonders, cities).process(gs.buildings)
            .also {
                log.atInfo()
                    .setMessage("Processed building data.")
                    .addKeyValue("size") { it.size }
                    .log()
            }
}