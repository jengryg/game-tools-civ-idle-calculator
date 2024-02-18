package game

import Logging
import game.data.*
import game.json.*
import logger
import utils.FileIo
import utils.JsonParser

class GameDataLoader : Logging {
    private val log = logger()

    private val ageDefinitionFile: String = "src/main/resources/game/ages.json"
    private val depositDefinitionFile: String = "src/main/resources/game/deposits.json"
    private val resourceDefinitionFile: String = "src/main/resources/game/resources.json"
    private val buildingDefinitionFile: String = "src/main/resources/game/buildings.json"
    private val cityDefinitionFile: String = "src/main/resources/game/cities.json"
    private val techDefinitionFile: String = "src/main/resources/game/techs.json"
    private val greatPersonDefinitionFile: String = "src/main/resources/game/persons.json"

    private val ages: Map<String, Age>
    private val deposits: Map<String, Deposit>
    private val resources: Map<String, Resource>
    private val buildings: Map<String, Building>
    private val technologies: Map<String, Technology>
    private val cities: Map<String, City>
    private val persons: Map<String, GreatPerson>

    private val gor: GameObjectRegistry

    init {
        ages = processAges(
            JsonParser.deserialize<Map<String, AgeJson>>(FileIo.readFile(ageDefinitionFile))
        )

        deposits = processDeposits(
            JsonParser.deserialize<Map<String, Boolean>>(FileIo.readFile(depositDefinitionFile))
        )

        resources = processResources(
            JsonParser.deserialize<Map<String, ResourceJson>>(FileIo.readFile(resourceDefinitionFile))
        )

        buildings = processBuildings(
            JsonParser.deserialize<Map<String, BuildingJson>>(FileIo.readFile(buildingDefinitionFile))
        )

        technologies = processTechnologyTree(
            JsonParser.deserialize<Map<String, TechJson>>(FileIo.readFile(techDefinitionFile))
        )

        cities = processCities(
            JsonParser.deserialize<Map<String, CityJson>>(FileIo.readFile(cityDefinitionFile))
        )

        persons = processPersons(
            JsonParser.deserialize<Map<String, GreatPersonJson>>(FileIo.readFile(greatPersonDefinitionFile))
        )

        gor = GameObjectRegistry(
            ages = ages,
            deposits = deposits,
            resources = resources,
            buildings = buildings,
            technologies = technologies,
            cities = cities,
            persons = persons
        )
    }


    fun getRegistry(): GameObjectRegistry {
        return gor
    }

    private fun processAges(parsed: Map<String, AgeJson>): Map<String, Age> {
        return parsed.map { (name, json) -> name to createAge(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed ages definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    private fun processDeposits(parsed: Map<String, Boolean>): Map<String, Deposit> {
        return parsed.map { (name, _) -> name to Deposit(name = name) }.toMap().also {
            log.atDebug()
                .setMessage("Processed deposits definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    private fun processResources(parsed: Map<String, ResourceJson>): Map<String, Resource> {
        return parsed.map { (name, json) -> name to createResource(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed resources definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    private fun processBuildings(parsed: Map<String, BuildingJson>): Map<String, Building> {
        return parsed.map { (name, json) -> name to createBuilding(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed buildings definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    private fun processCities(parsed: Map<String, CityJson>): Map<String, City> {
        return parsed.map { (name, json) -> name to createCity(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed city definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    private fun createAge(name: String, json: AgeJson): Age {
        return Age(
            name = name,
            id = json.idx,
            cols = (json.from..json.to)
        )
    }

    private fun createResource(name: String, json: ResourceJson): Resource {
        return Resource(
            name = name,
            canStore = json.canStore,
            canPrice = json.canPrice,
            fromDeposit = deposits.containsKey(name)
        )
    }

    private fun createBuilding(name: String, json: BuildingJson): Building {
        return Building(
            name = name,
            input = json.input.map { (rName, amount) ->
                rName to ResourceAmount(
                    resource = resources[rName]!!,
                    amount = amount.toLong()
                )
            }.toMap(),
            output = json.output.map { (rName, amount) ->
                rName to ResourceAmount(
                    resource = resources[rName]!!,
                    amount = amount.toLong()
                )
            }.toMap(),
            construction = json.construction?.map { (rName, amount) ->
                rName to ResourceAmount(
                    resource = resources[rName]!!,
                    amount = amount.toLong()
                )
            }?.toMap() ?: emptyMap(),
            deposit = json.deposit?.mapNotNull { (dName, required) ->
                if (required) dName to deposits[dName]!! else null
            }?.toMap() ?: emptyMap(),
            special = when (json.special) {
                "HQ" -> BuildingType.HQ
                "WorldWonder" -> BuildingType.WORLD_WONDER
                "NaturalWonder" -> BuildingType.NATURAL_WONDER
                else -> BuildingType.NORMAL
            }
        )
    }

    private fun processTechnologyTree(techsJson: Map<String, TechJson>): Map<String, Technology> {
        val agesByTechColumns = ages.flatMap { (_, age) ->
            age.cols.map { it to age }
        }.toMap()

        val processed = techsJson.filterValues { it.requireTech.isNullOrEmpty() }.map { (name, json) ->
            name to createTechnology(name, agesByTechColumns[json.column]!!, json, emptyMap())
        }.toMap().toMutableMap().also {
            log.atTrace()
                .setMessage("Technology tree construction iteration.")
                .addKeyValue("elements#") { it.size }
                .log()
        }

        while (processed.size < techsJson.size) {
            techsJson
                .filterKeys { name -> !processed.containsKey(name) }
                .filterValues { json -> json.requireTech?.all { rqName -> processed.containsKey(rqName) } ?: true }
                .map { (name, json) ->
                    name to createTechnology(name, agesByTechColumns[json.column]!!, json, processed)
                }.let {
                    processed.putAll(it)
                }

            log.atTrace()
                .setMessage("Technology tree construction iteration.")
                .addKeyValue("elements#") { processed.size }
                .log()

        }

        processed.values.forEach { applyTechnologyData(it) }

        return processed.toMap().also {
            log.atDebug()
                .setMessage("Processed technologies definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    private fun applyTechnologyData(technology: Technology) {
        technology.revealDeposit.forEach { it.technology = technology }

        technology.unlockBuilding.forEach {
            it.technology = technology
            it.output.values.forEach { ra ->
                ra.resource.also { resource ->
                    if (resource.technology == null) {
                        resource.technology = technology
                    } else {
                        if (resource.technology!!.column > technology.column) {
                            resource.technology = technology
                        }
                    }
                }
            }
        }
    }

    private fun createTechnology(
        name: String,
        age: Age,
        json: TechJson,
        knownTechnology: Map<String, Technology>
    ): Technology {
        return Technology(
            name = name,
            column = json.column,
            revealDeposit = json.revealDeposit?.map { deposits[it]!! } ?: emptyList(),
            unlockBuilding = json.unlockBuilding?.map { buildings[it]!! } ?: emptyList(),
            requireTechnologies = json.requireTech?.map { knownTechnology[it]!! } ?: emptyList(),
            age = age
        )
    }

    private fun createCity(name: String, json: CityJson): City {
        return City(
            name = name,
            size = json.size,
            deposits = json.deposits.map { (dName, amount) ->
                DepositAmount(
                    deposit = deposits[dName]!!,
                    amount = amount
                )
            },
            uniqueBuildings = json.uniqueBuildings.map { (bName, tName) ->
                buildings[bName]!!.apply {
                    technology = technologies[tName]!!
                }
            }
        )
    }

    private fun processPersons(parsed: Map<String, GreatPersonJson>): Map<String, GreatPerson> {
        return parsed.map { (name, json) -> name to createPerson(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed persons definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    private fun createPerson(name: String, json: GreatPersonJson): GreatPerson {
        return GreatPerson(
            name = name,
            value = json.value,
            age = ages[json.age]!!,
            stdBoost = json.boost?.let { createBoosts(it) }
        )
    }

    private fun createBoosts(json: Map<String, List<String>>): List<StandardBoost> {
        require(json.size == 2) { "Great Person standard boost has unexpected format." }

        val types = json["multipliers"].let {
            require(it != null) { "Great Person standard boost has unexpected format." }
            it
        }
        val targets = json["buildings"].let {
            require(it != null) { "Great Person standard boost has unexpected format." }
            it
        }

        return types.flatMap { type ->
            targets.map { target ->
                StandardBoost(
                    boostType = when (type) {
                        "output" -> BoostType.OUTPUT
                        "storage" -> BoostType.STORAGE
                        else -> throw IllegalArgumentException("Great Person standard boost has unexpected type multipler.")
                    },
                    boostTarget = buildings[target]!!
                )
            }
        }
    }
}