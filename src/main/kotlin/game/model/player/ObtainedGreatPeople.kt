package game.model.player

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.model.game.GreatPerson
import utils.io.JustNameSerializer

class ObtainedGreatPeople(
    @JsonSerialize(using = JustNameSerializer::class)
    val greatPerson: GreatPerson,
    val current: Int,
    val permanent: Int,
    val shards: Int,
    val currentEffect: Double,
    val permanentEffect: Double
) {
    val totalEffect: Double = currentEffect + permanentEffect
}