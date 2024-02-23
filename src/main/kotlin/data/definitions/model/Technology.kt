package data.definitions.model

import com.fasterxml.jackson.annotation.JsonIgnore
import common.StandardBoost

class Technology(
    val name: String,
    val column: Int,
    val age: Age,
    val stdBoost: List<StandardBoost>,
    @JsonIgnore
    val revealDeposit: List<Deposit>,
    @JsonIgnore
    val unlockBuilding: List<Building>,
    @JsonIgnore
    val requireTechnologies: List<Technology>,
)