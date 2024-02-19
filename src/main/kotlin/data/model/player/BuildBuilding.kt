package data.model.player

import data.model.definitions.Building
import common.ResourceAmount

class BuildBuilding(
    val building: Building,
    val level: Int,
    val desiredLevel: Int,
    val capacity: Int,
    val stockpileCapacity: Int,
    val stockpileMax: Int,
    val priority: Int,
    val options: Int,
    val electrification: Int,
    val status: BuildingStatus,
    val resources: List<ResourceAmount>
) {
    val totalCost = building.getTotalBuildingValueAtLevel(level)
}
