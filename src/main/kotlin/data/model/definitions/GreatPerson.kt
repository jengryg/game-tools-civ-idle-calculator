package data.model.definitions

import common.StandardBoost

class GreatPerson(
    val name: String,
    val stdBoost: List<StandardBoost>?,
    val value: Int,
    val age: Age
)