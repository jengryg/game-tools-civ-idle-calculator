package game.loader.player.json

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class TileWrapper(
    @JsonProperty("value")
    val rawValue: List<Any>
) {
    @JsonIgnore
    val value = rawValue.map { it ->
        (it as ArrayList<*>).let { al ->
            require(al.size == 2) { "Parsed elements from tiles must be of size 2 but has size ${al.size}!" }
            val id = al[0] as Int
            val data = al[1] as? LinkedHashMap<*, *>

            require(data != null) { "Parsed tile must not be null!" }

            TileJson(
                id = id,
                tile = data["tile"] as Int,
                explored = data["explored"] as Boolean,
                building = data["building"]?.let { it as LinkedHashMap<*, *> }?.let { building ->
                    TileBuildingJson(
                        level = building["level"]!! as Int,
                        desiredLevel = building["desiredLevel"] as Int,
                        status = building["status"] as String,
                        capacity = building["capacity"] as Int,
                        stockpileCapacity = building["stockpileCapacity"] as Int,
                        stockpileMax = building["stockpileMax"] as Int,
                        options = building["options"] as Int,
                        electrification = building["electrification"] as Int,
                        type = building["type"] as String,
                        resources = (building["resources"] as? LinkedHashMap<*, *>)?.map { resources ->
                            (resources.key as String) to when(resources.value) {
                                is Int -> (resources.value as Int).toDouble()
                                else -> (resources.value as Double)
                            }
                        }?.toMap() ?: emptyMap()
                    )
                },
                deposit = (data["deposit"] as? LinkedHashMap<*, *>)?.map {
                    it.key as String
                } ?: emptyList()
            )
        }
    }
}