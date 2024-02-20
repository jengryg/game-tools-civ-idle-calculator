package common

enum class BuildingType(
    val requiresUnlockTech: Boolean,
    val isAnyWonder: Boolean,
    val isHQ: Boolean
) {
    NORMAL(true, false, false),
    HQ(false, false, true),
    WORLD_WONDER(true, true, false),
    NATURAL_WONDER(false, true, false)
}

fun buildingTypeFromString(value: String?): BuildingType {
    return when (value) {
        "HQ" -> BuildingType.HQ
        "WorldWonder" -> BuildingType.WORLD_WONDER
        "NaturalWonder" -> BuildingType.NATURAL_WONDER
        else -> BuildingType.NORMAL
    }
}