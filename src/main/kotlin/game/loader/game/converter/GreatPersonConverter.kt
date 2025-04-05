package game.loader.game.converter

import game.loader.game.data.AgeData
import game.loader.game.data.BuildingMultiplier
import game.loader.game.data.GreatPersonData
import game.loader.game.json.GreatPersonBoostJson
import game.loader.game.json.GreatPersonJson

class GreatPersonConverter(
    private val ages: Map<String, AgeData>
) {
    fun process(greatPersons: Map<String, GreatPersonJson>): Map<String, GreatPersonData> {
        return greatPersons.mapValues { (name, json) -> create(name, json) }
    }

    private fun create(name: String, json: GreatPersonJson): GreatPersonData {
        return GreatPersonData(
            name = name,
            value = json.value,
            age = ages[json.age]!!,
            buildingMultipliers = json.boost?.let { createMultiplierTriples(it, json.value) } ?: emptyList()
        )
    }

    private fun createMultiplierTriples(
        greatPersonBoostJson: GreatPersonBoostJson,
        value: Double
    ): List<BuildingMultiplier> {
        return greatPersonBoostJson.buildings.flatMap { bName ->
            greatPersonBoostJson.multipliers.map { type ->
                BuildingMultiplier(name = bName, target = type, value = value)
            }
        }
    }
}