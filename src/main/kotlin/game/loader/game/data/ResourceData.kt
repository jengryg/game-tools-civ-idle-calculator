package game.loader.game.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class ResourceData(
    name: String,
    val canStore: Boolean,
    val canPrice: Boolean,
    @JsonSerialize(using = JustNameSerializer::class)
    val deposit: DepositData? = null
) : HasNameBase(name) {
    val isScience = name == "Science"
    val isWorker = name == "Worker"

    @JsonSerialize(using = JustNameSerializer::class)
    var unlockedBy: TechnologyData? = null
    var tier: Int? = null
    var price: Double? = null

    val producer: MutableList<BuildingData> = mutableListOf()
}