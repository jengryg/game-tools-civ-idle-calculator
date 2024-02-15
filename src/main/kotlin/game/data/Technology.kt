package game.data

import com.fasterxml.jackson.annotation.JsonIgnore

class Technology(
    val name: String,
    val column: Int,
    @JsonIgnore
    val revealDeposit: List<Deposit>,
    @JsonIgnore
    val unlockBuilding: List<Building>,
    @JsonIgnore
    val requireTechnologies: List<Technology>,
    val age: Age
)