package game.model.player

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import game.common.BuildingStatus
import game.model.game.Building
import game.model.game.Resource
import utils.io.HasIdBase
import utils.io.JustNameSerializer

class TileBuilding(
    id: Int,
    @JsonSerialize(using = JustNameSerializer::class)
    val bld: Building,
    val level: Int,
    val desiredLevel: Int,
    val capacity: Int,
    val stockpileCapacity: Int,
    val stockpileMax: Int,
    val options: Int,
    val electrification: Int,
    val status: BuildingStatus,
    val resources: Map<Resource, Double>
) : HasIdBase(id)