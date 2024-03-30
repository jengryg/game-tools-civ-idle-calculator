package game.loader.player.converter

import game.loader.game.GameData
import game.loader.player.data.ObtainedGreatPeopleData
import game.loader.player.json.PermanentPeopleJson

class ObtainedGreatPersonConverter(
    private val gameData: GameData,
) {
    fun process(
        current: Map<String, Int>,
        permanent: Map<String, PermanentPeopleJson>
    ): Map<String, ObtainedGreatPeopleData> {
        return gameData.greatPersons.mapValues { (name, person) ->
            val perm = permanent[name] ?: PermanentPeopleJson(level = 0, amount = 0)
            ObtainedGreatPeopleData(
                greatPerson = person,
                current = current[name] ?: 0,
                permanent = perm.level,
                shards = perm.amount
            )
        }
    }
}