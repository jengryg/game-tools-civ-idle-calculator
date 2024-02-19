package data.model.definitions

class Resource(
    val name: String,
    val canStore: Boolean,
    val canPrice: Boolean,
    val fromDeposit: Boolean
) {
    var technology: Technology? = null
    var tier: Int? = null
    var price: Double? = null
}