package game.common

enum class BuildingStatus {
    BUILDING, UPGRADING, PAUSED, COMPLETED;

    companion object {
        fun fromString(value: String): BuildingStatus {
            return when (value.lowercase()) {
                "building" -> BUILDING
                "upgrading" -> UPGRADING
                "paused" -> PAUSED
                "completed" -> COMPLETED
                else -> throw IllegalArgumentException("Invalid value provided for ${this::class.simpleName}: $value")
            }
        }
    }
}