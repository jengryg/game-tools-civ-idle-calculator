package game.loader.game.converter

import game.loader.game.data.DepositData
import game.loader.game.data.ResourceData
import game.loader.game.json.ResourceJson

class ResourcesConverter(
    private val deposits: Map<String, DepositData>
) {
    fun process(resources: Map<String, ResourceJson>): Map<String, ResourceData> {
        return resources.mapValues { (name, json) -> create(name, json) }
    }

    private fun create(name: String, json: ResourceJson): ResourceData {
        return ResourceData(
            name = name,
            canStore = json.canStore!!,
            canPrice = json.canPrice!!,
            deposit = deposits[name]
            // If a resource has the same name as a deposit, then the resource is considered to be produced from that deposit.
        )
    }
}