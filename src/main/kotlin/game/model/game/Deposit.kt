package game.model.game

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import utils.io.HasNameBase
import utils.io.JustNameSerializer

class Deposit(
    name: String,
    @JsonSerialize(using = JustNameSerializer::class)
    val revealedBy: Technology
) : HasNameBase(name)