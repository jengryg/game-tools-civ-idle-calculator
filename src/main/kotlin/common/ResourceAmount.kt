package common

import data.definitions.model.Resource

open class ResourceAmount(
    val resource: Resource,
    var amount: Long = 0L
) {
    fun enterpriseValue(): Double {
        return if (resource.canPrice && resource.name !in listOf("Worker", "Power", "Science", "Warp", "Explorer")) {
            resource.price!! * amount.toDouble()
        } else {
            0.0
        }
    }
}