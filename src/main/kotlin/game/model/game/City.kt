package game.model.game

import utils.io.HasNameBase

class City(
    name: String,
    val size: Int,
    val depositDensity: Map<Deposit, Double>
) : HasNameBase(name)