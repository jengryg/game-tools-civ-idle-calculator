package game.loader.game.converter

import game.common.modifiers.BuildingModTarget
import game.loader.game.data.WonderData
import game.loader.game.json.WonderJson

class WonderConverter {
    fun process(wonders: Map<String, List<WonderJson>>): Map<String, List<WonderData>> {
        return wonders.mapValues { (name, json) -> create(name, json) }
    }

    private fun create(name: String, json: List<WonderJson>): List<WonderData> {
        return json.map {
            WonderData(
                name = name,
                targetBuildingName = it.target,
                multipliers = it.mods.mapKeys { (k, _) -> BuildingModTarget.fromString(k) }
            )
        }
    }
}