package data.player

import data.definitions.model.City
import data.definitions.model.Technology
import data.definitions.model.Wonder
import data.player.model.ActiveGreatPerson
import data.player.model.MapTile

interface IPlayerState {
    val city: City
    val greatPeople: Map<String, ActiveGreatPerson>
    val unlockedTechnology: Map<String, Technology>
    val tiles: Map<Int, MapTile>
    val activeWonders: Map<String, Wonder>
}