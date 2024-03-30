package game.model.game

import utils.io.HasNameBase

class Age(
    name: String,
    val id: Int,
    val cols: IntRange
) : HasNameBase(name)