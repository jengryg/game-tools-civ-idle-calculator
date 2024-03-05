package common

enum class BuildingType(
    val requiresUnlockTech: Boolean,
    val isAnyWonder: Boolean,
    val isHQ: Boolean,
    val isWorldWonder: Boolean
) {
    NORMAL(true, false, false, false),
    HQ(false, false, true, false),
    WORLD_WONDER(true, true, false, true),
    NATURAL_WONDER(false, true, false, false)
}

fun buildingTypeFromString(value: String?): BuildingType {
    return when (value) {
        "HQ" -> BuildingType.HQ
        "WorldWonder" -> BuildingType.WORLD_WONDER
        "NaturalWonder" -> BuildingType.NATURAL_WONDER
        else -> BuildingType.NORMAL
    }
}