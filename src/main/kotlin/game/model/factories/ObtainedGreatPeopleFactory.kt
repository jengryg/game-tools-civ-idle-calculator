package game.model.factories

import game.loader.player.data.ObtainedGreatPeopleData
import game.model.game.GreatPerson
import game.model.player.ObtainedGreatPeople

class ObtainedGreatPeopleFactory(
    private val greatPeople: Map<String, GreatPerson>
) {
    fun process(obtainedGreatPeople: Map<String, ObtainedGreatPeopleData>): Map<String, ObtainedGreatPeople> {
        return obtainedGreatPeople.mapValues { (_, data) -> create(data) }
    }

    private fun create(data: ObtainedGreatPeopleData): ObtainedGreatPeople {
        val person = greatPeople[data.greatPerson.name]!!
        return ObtainedGreatPeople(
            greatPerson = person,
            current = data.current,
            permanent = data.permanent,
            shards = data.shards,
            currentEffect = data.currentEffect,
            permanentEffect = data.permanentEffect
        )
    }
}