package game.model.factories

import game.loader.game.data.ResourceData
import game.model.game.Deposit
import game.model.game.Resource
import game.model.game.Technology

class ResourceFactory(
    private val deposits: Map<String, Deposit>,
    private val technologies: Map<String, Technology>
) {
    fun process(resources: Map<String, ResourceData>): Map<String, Resource> {
        return resources.mapValues { (_, data) -> create(data) }
    }

    fun create(data: ResourceData): Resource {
        return Resource(
            name = data.name,
            canStore = data.canStore,
            canPrice = data.canPrice,
            deposit = data.deposit?.let { deposits[it.name]!! },
            isScience = data.isScience,
            unlockedBy = data.unlockedBy?.let { technologies[it.name]!! },
            tier = data.tier!!,
            price = data.price!!,
        )
    }
}