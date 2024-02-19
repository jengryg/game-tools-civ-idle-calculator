package common

import data.model.definitions.Building

class StandardBoost(
    val boostType: BoostType,
    val boostTarget: Building,
    val value: Double = 0.0
)