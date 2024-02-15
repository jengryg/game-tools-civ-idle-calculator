package game.data

enum class BuildingType(val requiresUnlockTech: Boolean) {
    NORMAL(true),
    HQ(false),
    WORLD_WONDER(true),
    NATURAL_WONDER(false)
}
