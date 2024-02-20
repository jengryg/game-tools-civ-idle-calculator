package data.definitions.model

import com.fasterxml.jackson.annotation.JsonIgnore

class Resource(
    val name: String,
    val canStore: Boolean,
    val canPrice: Boolean,
    val fromDeposit: Boolean
) {
    var technology: Technology? = null
    var tier: Int? = null
    var price: Double? = null

    @JsonIgnore
    fun isScience() : Boolean {
        return name == "Science"
    }
}