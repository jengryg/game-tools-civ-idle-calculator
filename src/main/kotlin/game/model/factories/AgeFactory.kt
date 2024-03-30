package game.model.factories

import game.loader.game.data.AgeData
import game.model.game.Age

class AgeFactory {
    fun process(ages: Map<String, AgeData>): Map<String, Age> {
        return ages.mapValues { (_, data) -> create(data) }
    }

    private fun create(data: AgeData): Age {
        return Age(
            name = data.name,
            id = data.id,
            cols = data.cols
        )
    }
}