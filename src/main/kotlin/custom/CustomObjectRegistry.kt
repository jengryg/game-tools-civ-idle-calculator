package custom

import custom.data.ActiveGreatPerson
import custom.data.MapTile
import game.data.City
import game.data.Technology

class CustomObjectRegistry(
    val city: City,
    val greatPeople: Map<String, ActiveGreatPerson>,
    val unlockedTechnologies: Map<String, Technology>,
    val tiles: Map<Int, MapTile>
)