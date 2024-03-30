package game.loader.game.converter

import game.loader.game.data.AgeData
import game.loader.game.data.GreatPersonData
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
            buildingMultipliers = json.boost?.flatMap { (bName, multi) ->
                multi.map { type -> Triple(bName, type, json.value.toDouble()) }
            } ?: emptyList()
        )
    }
}