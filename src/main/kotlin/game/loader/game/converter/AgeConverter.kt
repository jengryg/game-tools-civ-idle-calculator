package game.loader.game.converter

import game.loader.game.data.AgeData
import game.loader.game.json.AgeJson

class AgeConverter {
    fun process(ages: Map<String, AgeJson>): Map<String, AgeData> {
        return ages.mapValues { (name, json) -> create(name, json) }
    }

    private fun create(name: String, json: AgeJson): AgeData {
        return AgeData(
            name = name,
            id = json.idx,
            cols = (json.from..json.to)
        )
    }
}