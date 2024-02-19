package common

enum class BuildingType(val requiresUnlockTech: Boolean) {
    NORMAL(true),
    HQ(false),
    WORLD_WONDER(true),
    NATURAL_WONDER(false)
}


fun getBuildingTypeFromString(value: String?): BuildingType {
    return when (value) {
        "HQ" -> BuildingType.HQ
        "WorldWonder" -> BuildingType.WORLD_WONDER
        "NaturalWonder" -> BuildingType.NATURAL_WONDER
        else -> BuildingType.NORMAL
    }
}