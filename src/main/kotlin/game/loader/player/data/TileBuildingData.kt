package game.loader.player.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.BuildingStatus
import game.loader.game.data.BuildingData
import game.loader.game.data.ResourceData
import utils.io.HasIdBase
import utils.io.JustNameSerializer

class TileBuildingData(
    id: Int,
    @JsonSerialize(using = JustNameSerializer::class)
    val bld: BuildingData,
    val level: Int,
    val desiredLevel: Int,
    val capacity: Int,
    val stockpileCapacity: Int,
    val stockpileMax: Int,
    val options: Int,
    val electrification: Int,
    val status: BuildingStatus,
    val resources: Map<ResourceData, Double>,
) : HasIdBase(id)