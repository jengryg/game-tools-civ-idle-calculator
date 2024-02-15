package custom

import custom.data.MapTile
import game.data.City
import game.data.Deposit
import game.data.Technology

class CustomObjectRegistry(
    val city: City,
    val greatPeople: Map<String, Int>,
    val unlockedTechnologies: Map<String, Technology>,
    val tiles: Map<Int, MapTile>
)