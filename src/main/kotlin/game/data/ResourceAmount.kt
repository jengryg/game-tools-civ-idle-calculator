package game.data

class ResourceAmount(
    val resource: Resource,
    val amount: Long
) {
    fun value(): Double {
        return if (resource.canPrice) resource.price!! * amount.toDouble() else 0.0
    }
}