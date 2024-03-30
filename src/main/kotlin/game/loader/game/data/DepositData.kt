package game.loader.game.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class DepositData(
    name: String,
    @JsonSerialize(using = JustNameSerializer::class)
    val revealedBy: TechnologyData
) : HasNameBase(name)