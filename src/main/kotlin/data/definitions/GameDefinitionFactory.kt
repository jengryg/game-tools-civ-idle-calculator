package data.definitions

import Logging
import common.*
import data.definitions.json.*
import data.definitions.model.*
import logger
import utils.FileIo
import utils.JsonParser

class GameDefinitionFactory : Logging {
    private val log = logger()

    private val gameDefinitionPath = "src/main/resources/game"

    /**
     * [Age] definitions.
     */
    private val ages: Map<String, Age> = processAges(
        JsonParser.deserialize<Map<String, AgeJson>>(FileIo.readFile("$gameDefinitionPath/ages.json"))
    )

    private fun processAges(parsed: Map<String, AgeJson>): Map<String, Age> {
        return parsed.map { (name, json) -> name to createAge(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed ages definition data.")
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

    /**
     * [Deposit] definitions.
     */
    private val deposits: Map<String, Deposit> = processDeposits(
        JsonParser.deserialize<Map<String, Boolean>>(FileIo.readFile("$gameDefinitionPath/deposits.json"))
    )

    private fun processDeposits(parsed: Map<String, Boolean>): Map<String, Deposit> {
        return parsed.map { (name, _) -> name to Deposit(name = name) }.toMap().also {
            log.atDebug()
                .setMessage("Processed deposits definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    /**
     * [Resource] definitions for noPricing attribute.
     */
    private val noPriceResources =
        JsonParser.deserialize<Map<String, Boolean>>(FileIo.readFile("$gameDefinitionPath/resources-noPrice.json"))

    /**
     * [Resource] definitions for noStorage attribute.
     */
    private val noStorageResources =
        JsonParser.deserialize<Map<String, Boolean>>(FileIo.readFile("$gameDefinitionPath/resources-noStorage.json"))

    /**
     * [Resource] definitions.
     */
    private val resources: Map<String, Resource> = processResources(
        JsonParser.deserialize<Map<String, ResourceJson>>(FileIo.readFile("$gameDefinitionPath/resources.json"))
    )

    private fun processResources(parsed: Map<String, ResourceJson>): Map<String, Resource> {
        return parsed.map { (name, json) -> name to createResource(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed resources definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
    }

    private fun createResource(name: String, json: ResourceJson): Resource {
        return Resource(
            name = name,
            canStore = noStorageResources[name] ?: false,
            canPrice = noPriceResources[name] ?: false,
            fromDeposit = deposits.containsKey(name)
        )
    }

    /**
     * [Building] definitions.
     */
    private val buildings: Map<String, Building> = processBuildings(
        JsonParser.deserialize<Map<String, BuildingJson>>(FileIo.readFile("$gameDefinitionPath/buildings.json"))
    )

    private fun processBuildings(parsed: Map<String, BuildingJson>): Map<String, Building> {
        return parsed.map { (name, json) -> name to createBuilding(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed buildings definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
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
            special = buildingTypeFromString(json.special)
        )
    }

    /**
     * [Technology] definitions.
     */
    private val technologies: Map<String, Technology> = processTechnologyTree(
        JsonParser.deserialize<Map<String, TechJson>>(FileIo.readFile("$gameDefinitionPath/techs.json"))
    )

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
            age = age,
            stdBoost = json.buildingMultiplier?.flatMap {
                createStdBoostForTech(it.key, it.value)
            } ?: emptyList()
        )
    }

    private fun createStdBoostForTech(bName: String, boost: Map<String, Int>): List<StandardBoost> {
        return boost.map { (type, value) ->
            StandardBoost(
                boostTarget = buildings[bName]!!,
                boostType = when (type) {
                    "output" -> BoostType.OUTPUT
                    "input" -> BoostType.INPUT
                    "storage" -> BoostType.STORAGE
                    "worker" -> BoostType.WORKER
                    else -> throw IllegalArgumentException("Unknown boost type for technology parsing.")
                },
                value = value.toDouble()
            )
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

    /**
     * [City] definitions.
     */
    private val cities: Map<String, City> = processCities(
        JsonParser.deserialize<Map<String, CityJson>>(FileIo.readFile("$gameDefinitionPath/cities.json"))
    )

    private fun processCities(parsed: Map<String, CityJson>): Map<String, City> {
        return parsed.map { (name, json) -> name to createCity(name, json) }.toMap().also {
            log.atDebug()
                .setMessage("Processed city definition data.")
                .addKeyValue("elements#") { it.size }
                .log()
        }
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

    /**
     * [GreatPerson] definitions.
     */
    private val persons: Map<String, GreatPerson> = processPersons(
        JsonParser.deserialize<Map<String, GreatPersonJson>>(FileIo.readFile("$gameDefinitionPath/persons.json"))
    )

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

    fun getGameDefinition(): GameDefinition {
        return GameDefinition(
            ages = ages,
            deposits = deposits,
            resources = resources,
            buildings = buildings,
            technologies = technologies,
            cities = cities,
            persons = persons
        )
    }
}