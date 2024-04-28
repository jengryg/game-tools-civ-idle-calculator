package game.model

import Logging
import game.model.game.*
import game.model.player.ObtainedGreatPeople
import game.model.player.Tile
import game.model.player.Transportation
import logger

class Model(
    val ages: Map<String, Age>,
    val buildings: Map<String, Building>,
    val cities: Map<String, City>,
    val deposits: Map<String, Deposit>,
    val greatPersons: Map<String, GreatPerson>,
    val resources: Map<String, Resource>,
    val technologies: Map<String, Technology>,
    val obtainedGreatPeoples: Map<String, ObtainedGreatPeople>,
    val tiles: Map<Int, Tile>,
    val transports: Map<Int, Transportation>,
    val currentCity: City,
    val unlockedTechnologies: Map<String, Technology>,
) : Logging {
    private val log = logger()

    init {
        checkResourceProducers()
    }

    private fun checkResourceProducers() {
        resources.forEach { (name, r) ->
            r.producer.size.let { size ->
                if (size != 1) {
                    log.atDebug()
                        .setMessage("Determination of resource producer is not unique.")
                        .addKeyValue("producer") { name }
                        .addKeyValue("size") { size }
                        .addKeyValue("choices") { r.producer.joinToString(" ") { it.name } }
                        .log()
                }
            }
        }
    }

    fun getAge(name: String): Age {
        return ages[name] ?: throw IllegalArgumentException("Requested unknown age $name from model.")
    }

    fun getBuilding(name: String): Building {
        return buildings[name] ?: throw IllegalArgumentException("Requested unknown building $name from model.")
    }

    fun getCity(name: String): City {
        return cities[name] ?: throw IllegalArgumentException("Requested unknown city $name from model.")
    }

    fun getDeposit(name: String): Deposit {
        return deposits[name] ?: throw IllegalArgumentException("Requested unknown deposit $name from model.")
    }

    fun getGreatPerson(name: String): GreatPerson {
        return greatPersons[name] ?: throw IllegalArgumentException("Requested unknown great person $name from model.")
    }

    fun getResource(name: String): Resource {
        return resources[name] ?: throw IllegalArgumentException("Requested unknown resource $name from model.")
    }

    fun getTechnology(name: String): Technology {
        return technologies[name] ?: throw IllegalArgumentException("Requested unknown technology $name from model.")
    }

    fun getObtainedGreatPeople(name: String): ObtainedGreatPeople {
        return obtainedGreatPeoples[name]
            ?: throw IllegalArgumentException("Requested unknown obtained great people $name from model.")
    }

    fun getTile(id: Int): Tile {
        return tiles[id]
            ?: throw IllegalArgumentException("Requested unknown tile $id from model.")
    }

    fun getTransport(id: Int): Transportation {
        return transports[id]
            ?: throw IllegalArgumentException("Requested unknown transport $id from model.")
    }

    fun getProducer(r: Resource): Building {
        return when (r.name) {
            "Worker" -> r.producer.firstOrNull { it.name == "Condo" }
            "Power" -> r.producer.firstOrNull { it.name == "HydroDam" }
            "Science" -> r.producer.firstOrNull { it.name == "School" }
            "Water" -> r.producer.firstOrNull { it.name == "HydroDam" }
            "Tool" -> r.producer.firstOrNull { it.name == "IronForge" }
            "Faith" -> r.producer.firstOrNull { it.name == "StPetersBasilica" }
            else -> return r.producer.single()
        } ?: throw IllegalStateException("Could not determine producer for resource ${r.name} from model.")
    }
}