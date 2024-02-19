package data.json.player

class TileBuildingJson(
    val level: Int,
    val desiredLevel: Int,
    val capacity: Int,
    val stockpileCapacity: Int,
    val stockpileMax: Int,
    val priority: Int,
    val options: Int,
    val electrification: Int,
    val status: String,
    val type: String,
    val resources: Map<String, Double>
)
