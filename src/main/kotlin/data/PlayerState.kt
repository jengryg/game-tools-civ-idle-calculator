package data

import data.model.player.ActiveGreatPerson
import data.model.player.MapTile
import data.model.definitions.City
import data.model.definitions.Technology
import data.model.definitions.Wonder

class PlayerState(
    override val city: City,
    override val greatPeople: Map<String, ActiveGreatPerson>,
    override val unlockedTechnologies: Map<String, Technology>,
    override val tiles: Map<Int, MapTile>,
    override val activeWonders: Map<String, Wonder>,
) : IPlayerState