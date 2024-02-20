package common

import data.definitions.model.Building

class StandardBoost(
    val boostType: BoostType,
    val boostTarget: Building,
    val value: Double = 0.0
)