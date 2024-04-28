package game.model.factories

import game.loader.game.data.BuildingData
import game.loader.game.data.ResourceData
import game.model.game.Building
import game.model.game.Deposit
import game.model.game.Resource
import game.model.game.Technology

class BuildingFactory(
    private val resources: Map<String, Resource>,
    private val technologies: Map<String, Technology>,
    private val deposits: Map<String, Deposit>
) {
    fun process(buildings: Map<String, BuildingData>): Map<String, Building> {
        return buildings.mapValues { (_, data) ->
            when (data.name) {
                "StPetersBasilica" -> createStPeters(data)
                else -> create(data)
            }.also { bld ->
                bld.output.keys.forEach {
                    it.producer.add(bld)
                }
            }
        }
    }

    private fun createStPeters(data: BuildingData): Building {
        return Building(
            name = data.name,
            input = createResourceAmountMap(data.input),
            output = mapOf(resources["Faith"]!! to 300_000.0),
            construction = createResourceAmountMap(data.construction),
            deposit = data.deposit.map {
                deposits[it.name]
                    ?: throw IllegalStateException("Can not obtain model for deposit with name ${it.name}!")
            },
            type = data.type,
            unlockedBy = data.unlockedBy?.let {
                technologies[it.name]
                    ?: throw IllegalStateException("Can not obtain model for technology with name ${it.name}!")
            },
            mods = data.mods.toList(),
            tier = data.tier ?: throw IllegalStateException("Building ${data.name} has tier null!"),
            cost = createResourceAmountMap(data.cost),
            activeMods = data.activeMods.toList()
        )
    }

    private fun create(data: BuildingData): Building {
        return Building(
            name = data.name,
            input = createResourceAmountMap(data.input),
            output = createResourceAmountMap(data.output),
            construction = createResourceAmountMap(data.construction),
            deposit = data.deposit.map {
                deposits[it.name]
                    ?: throw IllegalStateException("Can not obtain model for deposit with name ${it.name}!")
            },
            type = data.type,
            unlockedBy = data.unlockedBy?.let {
                technologies[it.name]
                    ?: throw IllegalStateException("Can not obtain model for technology with name ${it.name}!")
            },
            mods = data.mods.toList(),
            tier = data.tier ?: throw IllegalStateException("Building ${data.name} has tier null!"),
            cost = createResourceAmountMap(data.cost),
            activeMods = data.activeMods.toList()
        )
    }

    private fun createResourceAmountMap(amounts: Map<ResourceData, Double>): Map<Resource, Double> {
        return amounts.mapKeys { (r, _) ->
            resources[r.name] ?: throw IllegalStateException("Can not obtain model for resource ${r.name}!")
        }
    }
}