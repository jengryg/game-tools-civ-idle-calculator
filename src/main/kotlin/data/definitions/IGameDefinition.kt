package data.definitions

import data.definitions.model.*

interface IGameDefinition {
    val ages: Map<String, Age>
    val deposits: Map<String, Deposit>
    val resources: Map<String, Resource>
    val buildings: Map<String, Building>
    val technologies: Map<String, Technology>
    val cities: Map<String, City>
    val persons: Map<String, GreatPerson>
    val wonders: Map<String, Wonder>
}