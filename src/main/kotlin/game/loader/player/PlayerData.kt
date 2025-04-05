package game.loader.player

import game.loader.game.data.CityData
import game.loader.game.data.TechnologyData
import game.loader.player.data.ObtainedGreatPeopleData
import game.loader.player.data.TileData
import game.loader.player.data.TransportationData

class PlayerData(
    override val city: CityData,
    override val unlockedTechnology: Map<String, TechnologyData>,
    override val obtainedGreatPeople: Map<String, ObtainedGreatPeopleData>,
    override val tiles: Map<Int, TileData>,
    override val transportation: Map<Int, TransportationData>,
    override val ageWisdom: Map<String, Int>
) : IPlayerData