package game.loader.game

import game.loader.game.data.*

interface IGameData {
    val ages: Map<String, AgeData>
    val deposits: Map<String, DepositData>
    val resources: Map<String, ResourceData>
    val technologies: Map<String, TechnologyData>
    val greatPersons: Map<String, GreatPersonData>
    val buildings: Map<String, BuildingData>
    val cities: Map<String, CityData>
}