package data.player

import data.definitions.model.City
import data.definitions.model.Technology
import data.definitions.model.Wonder
import data.player.model.ActiveGreatPerson
import data.player.model.MapTile

class PlayerState(
    override val city: City,
    override val greatPeople: Map<String, ActiveGreatPerson>,
    override val unlockedTechnology: Map<String, Technology>,
    override val tiles: Map<Int, MapTile>,
    override val activeWonders: Map<String, Wonder>,
) : IPlayerState