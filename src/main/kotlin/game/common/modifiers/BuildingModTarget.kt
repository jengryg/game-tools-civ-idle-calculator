package game.common.modifiers

enum class BuildingModTarget {
    INPUT, OUTPUT, STORAGE, WORKER;

    companion object {
        fun fromString(value: String): BuildingModTarget {
            return when (value.lowercase()) {
                "input" -> INPUT
                "output" -> OUTPUT
                "storage" -> STORAGE
                "worker" -> WORKER
                else -> throw IllegalArgumentException("Invalid value provided for ${this::class.simpleName}: $value")
            }
        }
    }
}