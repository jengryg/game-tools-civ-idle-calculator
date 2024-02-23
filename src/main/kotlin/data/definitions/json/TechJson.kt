package data.definitions.json

class TechJson(
    val column: Int,
    val revealDeposit: List<String>?,
    val unlockBuilding: List<String>?,
    val requireTech: List<String>?,
    val buildingMultiplier: Map<String, Map<String, Int>>?,
)