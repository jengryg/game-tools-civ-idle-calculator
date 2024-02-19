package data

import data.model.definitions.City
import data.model.definitions.Technology
import data.model.definitions.Wonder
import data.model.player.ActiveGreatPerson
import data.model.player.MapTile

interface IPlayerState {
    val city: City
    val greatPeople: Map<String, ActiveGreatPerson>
    val unlockedTechnologies: Map<String, Technology>
    val tiles: Map<Int, MapTile>
    val activeWonders: Map<String, Wonder>
}