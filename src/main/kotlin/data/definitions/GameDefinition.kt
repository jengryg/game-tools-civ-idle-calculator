package data.definitions

import data.definitions.extension.GameDataCalculator
import data.definitions.extension.WonderDataFactory
import data.definitions.model.*

class GameDefinition(
    override val ages: Map<String, Age>,
    override val deposits: Map<String, Deposit>,
    override val resources: Map<String, Resource>,
    override val buildings: Map<String, Building>,
    override val technologies: Map<String, Technology>,
    override val cities: Map<String, City>,
    override val persons: Map<String, GreatPerson>,
) : IGameDefinition {
    override val wonders: Map<String, Wonder> = WonderDataFactory(this).getWonderEffects()

    init {
        GameDataCalculator.calculateTierAndPrice(this)
    }
}