package game.loader.game.converter

import Logging
import game.common.BuildingType
import game.common.modifiers.BuildingMod
import game.common.modifiers.BuildingModTarget
import game.common.modifiers.BuildingModType
import game.loader.game.data.*
import game.loader.game.json.BuildingJson
import logger

class BuildingConverter(
    private val resources: Map<String, ResourceData>,
    private val deposits: Map<String, DepositData>,
    private val technologies: Map<String, TechnologyData>,
    private val greatPersons: Map<String, GreatPersonData>,
    private val wonders: Map<String, List<WonderData>>,
    private val cities: Map<String, CityData>,
) : Logging {
    private val log = logger()

    private val results = mutableMapOf<String, BuildingData>()

    fun process(buildings: Map<String, BuildingJson>): Map<String, BuildingData> {
        buildings.filterValues { isWonder(it.special) }.forEach { (name, json) ->
            results[name] = createBuilding(name, json)
        }

        buildings.filterValues { !isWonder(it.special) }.forEach { (name, json) ->
            results[name] = createBuilding(name, json)
        }

        return results
    }

    private fun isWonder(special: String?): Boolean {
        return BuildingType.fromString(special) in listOf(
            BuildingType.WORLD_WONDER,
            BuildingType.NATURAL_WONDER
        )
    }

    private fun createBuilding(name: String, json: BuildingJson): BuildingData {
        val specialCity = getSpecialCity(name)

        return BuildingData(
            name = name,
            input = createResourceAmountMap(json.input),
            output = createResourceAmountMap(json.output),
            construction = json.construction?.let { createResourceAmountMap(it) } ?: emptyMap(),
            deposit = json.deposit?.mapNotNull { (d, b) ->
                if (b) deposits[d]!! else null
            } ?: emptyList(),
            type = BuildingType.fromString(json.special),
            unlockedBy = getUnlockTechnology(name, specialCity),
            specialCity = specialCity,
            mods = getBuildingMods(name),
        )
    }

    private fun getSpecialCity(buildingName: String): CityData? {
        cities.filterValues { it.uniqueBuildingUnlocks.containsKey(buildingName) }.let {
            when {
                it.isEmpty() -> return null
                it.size == 1 -> return it.values.single()
                else -> throw IllegalStateException("Building $buildingName is special in multiple cities: ${it.values.map { c -> c.name }}!")
            }
        }
    }

    private fun getUnlockTechnology(buildingName: String, specialCity: CityData?): TechnologyData? {
        return if (specialCity != null) {
            technologies[specialCity.uniqueBuildingUnlocks[buildingName]!!.name].also {
                if (it == null) {
                    throw IllegalStateException("Building $buildingName is special to city ${specialCity.name} but the specified unlock tech is invalid.")
                }
            }
        } else {
            technologies.values.filter { it.unlocksBuildingNames.contains(buildingName) }.also {
                if (it.size > 1) {
                    throw IllegalStateException("Building $buildingName is unlocked by more than one technology $it!")
                }
                // Note: The HQ and the natural wonders are not unlocked by any technology.
            }.firstOrNull()
        }
    }

    private fun createResourceAmountMap(amounts: Map<String, Int>): Map<ResourceData, Double> {
        return amounts.map { (r, a) ->
            (resources[r] ?: throw IllegalArgumentException("Requested unknown resource $r.")) to a.toDouble()
        }.toMap()
    }

    private fun getBuildingMods(buildingName: String): MutableList<BuildingMod> {
        val modsFromTech = technologies.values.flatMap { tech ->
            tech.buildingMultipliers.filter { m -> m.first == buildingName }.map {
                BuildingMod(
                    from = tech.name,
                    bldName = buildingName,
                    type = BuildingModType.TECHNOLOGY,
                    target = BuildingModTarget.fromString(it.second),
                    value = it.third
                ).also { bm -> tech.mods.add(bm) }
            }
        }

        val modsFromPersons = greatPersons.values.flatMap { person ->
            person.buildingMultipliers.filter { m -> m.first == buildingName }.map {
                BuildingMod(
                    from = person.name,
                    bldName = buildingName,
                    type = BuildingModType.GREAT_PERSON,
                    target = BuildingModTarget.fromString(it.second),
                    value = it.third
                ).also { bm -> person.mods.add(bm) }
            }
        }

        val modsFromWonders =
            wonders.values.flatten().filter { it.targetBuildingName == buildingName }.flatMap { wonder ->
                wonder.multipliers.map { (t, a) ->
                    BuildingMod(
                        from = wonder.name,
                        bldName = buildingName,
                        type = BuildingModType.WONDER,
                        target = t,
                        value = a
                    ).also { bm -> wonder.mods.add(bm) }
                }
            }

        return modsFromPersons.plus(modsFromTech).plus(modsFromWonders).toMutableList().also {
            log.atTrace()
                .setMessage("Apply modifications to building.")
                .addKeyValue("name") { buildingName }
                .addKeyValue("size") { it.size }
                .addKeyValue("targets") { it.map { it.target } }
                .log()
        }
    }
}