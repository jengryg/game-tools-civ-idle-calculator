package game.loader.game

import game.loader.game.json.*

class GameJson(
    val ages: Map<String, AgeJson>,
    val buildings: Map<String, BuildingJson>,
    val cities: Map<String, CityJson>,
    val deposits: Map<String, Boolean>,
    val greatPeople: Map<String, GreatPersonJson>,
    val resources: Map<String, ResourceJson>,
    val technologies: Map<String, TechJson>,
    val wonders: Map<String, List<WonderJson>>,
)