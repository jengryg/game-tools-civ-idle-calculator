package game.loader.player

import game.loader.game.data.CityData
import game.loader.game.data.TechnologyData
import game.loader.player.data.ObtainedGreatPeopleData
import game.loader.player.data.TileData
import game.loader.player.data.TransportationData

interface IPlayerData {
    val city: CityData
    val unlockedTechnology: Map<String, TechnologyData>
    val obtainedGreatPeople: Map<String, ObtainedGreatPeopleData>
    val tiles: Map<Int, TileData>
    val transportation: Map<Int, TransportationData>
    val ageWisdom: Map<String, Int>
}