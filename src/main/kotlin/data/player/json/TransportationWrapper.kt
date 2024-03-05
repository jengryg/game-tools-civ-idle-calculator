package data.player.json

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class TransportationWrapper(
    @JsonProperty("value")
    val rawValue: List<Any>
) {
    @JsonIgnore
    val value = rawValue.mapNotNull { it ->
        (it as ArrayList<*>).let { al ->
            require(al.size == 2) { "Parsed elements from transportation must be of size 2 but has size ${al.size}!" }
            val id = al[0] as Int
            val data = al[1] as? ArrayList<*>

            require(data != null) { "Parsed transportation ArrayList must not be null!" }

            if (data.isEmpty()) {
                null
            } else {
                data.map {
                    (it as LinkedHashMap<*, *>).let { transport ->
                        TransportJson(
                            id = transport["id"] as Int,
                            fromXy = transport["fromXy"] as Int,
                            toXy = transport["toXy"] as Int,
                            fromPosition = (transport["fromPosition"] as LinkedHashMap<*, *>).let { xy ->
                                Pair(xy["x"].toString().toDouble(), xy["y"].toString().toDouble())
                            },
                            toPosition = (transport["toPosition"] as LinkedHashMap<*, *>).let { xy ->
                                Pair(xy["x"].toString().toDouble(), xy["y"].toString().toDouble())
                            },
                            ticksRequired = transport["ticksRequired"] as Int,
                            ticksSpent = transport["ticksSpent"] as Int,
                            resource = transport["resource"] as String,
                            amount = transport["amount"].toString().toDouble(),
                            fuel = transport["fuel"] as String,
                            fuelAmount = transport["fuelAmount"].toString().toDouble(),
                            currentFuelAmount = transport["currentFuelAmount"].toString().toDouble(),
                            hasEnoughFuel = transport["hasEnoughFuel"].toString().toBoolean(),
                        )
                    }
                }
            }
        }
    }.flatten()
}
