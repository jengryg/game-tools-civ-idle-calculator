package game.loader.game.data

import utils.io.HasNameBase

open class AgeData(
    name: String,
    val id: Int,
    val cols: IntRange
) : HasNameBase(name)