package game.loader.player.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.loader.game.data.GreatPersonData
import utils.io.JustNameSerializer

class ObtainedGreatPeopleData(
    @JsonSerialize(using = JustNameSerializer::class)
    val greatPerson: GreatPersonData,
    val current: Int,
    val permanent: Int,
    val shards: Int,
) {
    val currentEffect = if (current <= 1) current.toDouble() else (1..current).sumOf { i -> 1.0 / i.toDouble() }
    val permanentEffect = permanent.toDouble()
    val totalEffect = currentEffect + permanentEffect
}