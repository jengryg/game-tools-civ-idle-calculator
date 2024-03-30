package game.model.factories

import game.loader.game.data.CityData
import game.model.game.City
import game.model.game.Deposit

class CityFactory(
    private val deposits: Map<String, Deposit>
) {
    fun process(cities: Map<String, CityData>): Map<String, City> {
        return cities.mapValues { (_, data) -> create(data) }
    }

    fun create(data: CityData): City {
        return City(
            name = data.name,
            size = data.size,
            depositDensity = data.depositDensity.mapKeys { (depData, _) -> deposits[depData.name]!! }
        )
    }
}