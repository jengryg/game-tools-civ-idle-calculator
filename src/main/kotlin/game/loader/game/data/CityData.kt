package game.loader.game.data

import utils.io.HasNameBase

class CityData(
    name: String,
    val size: Int,
    val depositDensity: Map<DepositData, Double>,
    val uniqueBuildingUnlocks: Map<String, TechnologyData>
) : HasNameBase(name)