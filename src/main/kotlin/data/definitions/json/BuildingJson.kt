package data.definitions.json

class BuildingJson(
    val input: Map<String, Int>,
    val output: Map<String, Int>,
    val construction: Map<String, Int>?,
    val deposit: Map<String, Boolean>?,
    val special: String?,
)