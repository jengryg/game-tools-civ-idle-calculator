package data.player.model

import common.ResourceAmount
import data.definitions.model.Building

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
    val investedResources = building.getCostForUpgradingLevelsFromTo(0, level)
    val investedEnterpriseValue = investedResources.mapValues { it.value.enterpriseValue() }
    val storedEnterpriseValue = resources.associate { it.resource.name to it.enterpriseValue() }
}
