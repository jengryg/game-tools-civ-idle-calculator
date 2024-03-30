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
            currentEffect = getCurrentEffect(data.current, person.value),
            permanentEffect = getPermanentEffect(data.permanent, person.value)
        )
    }

    private fun getCurrentEffect(current: Int, gpValue: Int): Double {
        val count = if (current <= 1) current.toDouble() else (1..current).sumOf { i -> 1.0 / i.toDouble() }
        return count * gpValue.toDouble()
    }


    private fun getPermanentEffect(permanent: Int, gpValue: Int) = permanent.toDouble() * gpValue.toDouble()
}