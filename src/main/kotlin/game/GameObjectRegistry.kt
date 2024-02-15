package game

import game.data.*

class GameObjectRegistry(
    val ages: Map<String, Age>,
    val deposits: Map<String, Deposit>,
    val resources: Map<String, Resource>,
    val buildings: Map<String, Building>,
    val technologies: Map<String, Technology>,
    val cities: Map<String, City>
)