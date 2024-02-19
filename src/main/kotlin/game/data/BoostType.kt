package game.data

enum class BoostType {
    /**
     * Increases resource consumption
     */
    INPUT,

    /**
     * Increases resource production
     */
    OUTPUT,

    /**
     * Increases storage capacity
     */
    STORAGE,

    /**
     * Increases the amount of required workers to run the building.
     * No effect on throughput of building.
     */
    WORKER,
    // TODO check implementation
}