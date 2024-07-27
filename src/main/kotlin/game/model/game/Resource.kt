package game.model.game

import com.fasterxml.jackson.annotation.JsonIgnore
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
    val isWorker: Boolean,
    @JsonSerialize(using = JustNameSerializer::class)
    val unlockedBy: Technology? = null,
    val tier: Int,
    val price: Double,
) : HasNameBase(name) {
    val producer: MutableList<Building> = mutableListOf()

    companion object {
        fun getEvOf(map: Map<Resource, Double>): Double {
            return map.map { (r, d) -> d * r.price }.sum()
        }
    }

    @JsonIgnore
    fun getSingleProducerOrThrow(): Building {
        if (producer.isEmpty()) {
            throw IllegalArgumentException("Resource $name has no known producer.")
        }
        if (producer.size > 1) {
            throw IllegalArgumentException("Resource $name has more than one producer: ${producer.joinToString(", ") { it.name }}")
        }
        return producer[0]
    }
}