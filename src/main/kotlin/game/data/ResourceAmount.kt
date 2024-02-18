package game.data

open class ResourceAmount(
    val resource: Resource,
    val amount: Long
) {
    fun value(): Double {
        return if (resource.canPrice) resource.price!! * amount.toDouble() else 0.0
    }

    fun enterpriseValue() : Double {
        return if(resource.canPrice && resource.name !in listOf("Worker", "Power", "Science", "Warp")) {
            resource.price!! * amount.toDouble()
        } else {
            0.0
        }
    }
}