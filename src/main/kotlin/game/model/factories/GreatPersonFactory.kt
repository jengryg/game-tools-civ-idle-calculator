package game.model.factories

import game.loader.game.data.GreatPersonData
import game.model.game.Age
import game.model.game.GreatPerson

class GreatPersonFactory(
    private val ages: Map<String, Age>
) {
    fun process(greatPersons: Map<String, GreatPersonData>): Map<String, GreatPerson> {
        return greatPersons.mapValues { (_, data) -> create(data) }
    }

    fun create(data: GreatPersonData): GreatPerson {
        return GreatPerson(
            name = data.name,
            value = data.value,
            age = ages[data.age.name]!!,
            mods = data.mods.toList()
        )
    }
}