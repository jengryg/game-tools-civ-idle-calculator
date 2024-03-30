package game.loader.game.converter

import game.loader.game.data.CityData
import game.loader.game.data.DepositData
import game.loader.game.data.TechnologyData
import game.loader.game.json.CityJson

class CityConverter(
    private val deposits: Map<String, DepositData>,
    private val technologies: Map<String, TechnologyData>
) {
    fun process(cities: Map<String, CityJson>): Map<String, CityData> {
        return cities.mapValues { (name, json) -> create(name, json) }
    }

    private fun create(name: String, json: CityJson): CityData {
        return CityData(
            name = name,
            size = json.size,
            depositDensity = json.deposits.map { (d, a) -> deposits[d]!! to a }.toMap(),
            uniqueBuildingUnlocks = json.uniqueBuildings.mapValues { (_, t) -> technologies[t]!! }
        )
    }
}