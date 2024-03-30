package game.loader.game

import game.loader.game.data.*

class GameData(
    override val ages: Map<String, AgeData>,
    override val deposits: Map<String, DepositData>,
    override val resources: Map<String, ResourceData>,
    override val technologies: Map<String, TechnologyData>,
    override val greatPersons: Map<String, GreatPersonData>,
    override val buildings: Map<String, BuildingData>,
    override val cities: Map<String, CityData>,
) : IGameData