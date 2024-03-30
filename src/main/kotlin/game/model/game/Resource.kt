package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class Resource(
    name: String,
    val canStore: Boolean,
    val canPrice: Boolean,
    @JsonSerialize(using = JustNameSerializer::class)
    val deposit: Deposit? = null,
    val isScience: Boolean,
    @JsonSerialize(using = JustNameSerializer::class)
    val unlockedBy: Technology? = null,
    val tier: Int,
    val price: Double,
) : HasNameBase(name) {
    val producer: MutableList<Building> = mutableListOf()
}