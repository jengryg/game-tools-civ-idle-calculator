package data.player.json

class TileBuildingJson(
    val level: Int,
    val desiredLevel: Int,
    val capacity: Int,
    val stockpileCapacity: Int,
    val stockpileMax: Int,
    val options: Int,
    val electrification: Int,
    val status: String,
    val type: String,
    val resources: Map<String, Double>
)
