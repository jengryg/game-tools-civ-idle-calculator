package game.common

enum class BuildingType {
    HQ, NORMAL, WORLD_WONDER, NATURAL_WONDER;

    companion object {
        fun fromString(value: String?): BuildingType {
            return when (value?.lowercase()) {
                "hq" -> HQ
                "worldwonder" -> WORLD_WONDER
                "naturalwonder" -> NATURAL_WONDER
                null -> NORMAL
                else -> throw IllegalArgumentException("Invalid value provided for ${this::class.simpleName}: $value")
            }
        }
    }
}