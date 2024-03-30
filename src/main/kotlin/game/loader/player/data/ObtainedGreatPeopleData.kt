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
)