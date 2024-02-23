package analysis.processors.chain

import CHAIN_TEXT_INDENT
import CHAIN_TEXT_LINE_START
import common.BoostType
import data.definitions.model.Building
import data.definitions.model.Technology
import data.definitions.model.Wonder
import data.player.model.ActiveGreatPerson
import org.slf4j.spi.LoggingEventBuilder

class ChainNode(
    val id: Int = 0,
    val building: Building,
    val alpMulti: Double,
    val affectedByPersons: List<ActiveGreatPerson>,
    val affectedByWonders: List<Wonder>,
    val affectedByTechnologies: List<Technology>,
) {
    val baseInput: Map<String, ChainNodeIO> = building.input.map { (_, ra) ->
        ra.resource.name to ChainNodeIO(
            resource = ra.resource,
            rawAmount = ra.amount,
            effects = affectedByPersons.filter { it.person.stdBoost?.any { b -> b.boostType == BoostType.INPUT } == true }
                .associate { it.person.name to (it.level * it.person.value).toDouble() },
            wonders = affectedByWonders.filter { it.stdBoost?.any { b -> b.boostType == BoostType.INPUT } == true }
                .flatMap { w ->
                    w.stdBoost!!.filter { it.boostType == BoostType.INPUT }.map { w.name to it.value }
                }.toMap(),
            technologies = affectedByTechnologies.filter { it.stdBoost.any { b -> b.boostType == BoostType.INPUT } }
                .flatMap { t ->
                    t.stdBoost.filter { it.boostType == BoostType.INPUT }.map { t.name to it.value }
                }.toMap(),
            alpMulti = alpMulti
        )
    }.toMap()

    val baseOutput: Map<String, ChainNodeIO> = building.output.map { (_, ra) ->
        ra.resource.name to ChainNodeIO(
            resource = ra.resource,
            rawAmount = ra.amount,
            effects = affectedByPersons.filter { it.person.stdBoost?.any { b -> b.boostType == BoostType.OUTPUT } == true }
                .associate { it.person.name to (it.level * it.person.value).toDouble() },
            wonders = affectedByWonders.filter { it.stdBoost?.any { b -> b.boostType == BoostType.OUTPUT } == true }
                .flatMap { w ->
                    w.stdBoost!!.filter { it.boostType == BoostType.OUTPUT }.map { w.name to it.value }
                }.toMap(),
            technologies = affectedByTechnologies.filter { it.stdBoost.any { b -> b.boostType == BoostType.OUTPUT } }
                .flatMap { t ->
                    t.stdBoost.filter { it.boostType == BoostType.OUTPUT }.map { t.name to it.value }
                }.toMap(),
            alpMulti = alpMulti
        )
    }.toMap()

    val inboundConnections = mutableListOf<ChainLink>()
    val outboundConnections = mutableListOf<ChainLink>()

    var count: Double = 0.0

    fun logging(builder: LoggingEventBuilder): LoggingEventBuilder {
        return builder
            .addKeyValue("ChainNode#Id") { id }
            .addKeyValue("ChainNode#building") { building.name }
            .addKeyValue("ChainNode#affectedBy") { affectedByPersons.map { "${it.person.name} ${it.level}" } }
            .addKeyValue("ChainNode#baseInput") {
                baseInput.values.map { it }.toTypedArray().contentToString()
            }
            .addKeyValue("ChainNode#baseOutput") {
                baseOutput.values.map { it }.toTypedArray().contentToString()
            }
            .addKeyValue("ChainNode#count") { count }
    }

    fun text(indent: Int): String {
        val block = listOfNotNull(
            """
            *----------------------------------------------------------------------------------------------------
            ${CHAIN_TEXT_LINE_START.replace("%type", " ")} ${building.name}: $count
            """.trimIndent(),
            baseOutput.mapNotNull { it.value.text('+').takeIf { s -> s.isNotBlank() } }.joinToString("\n")
                .takeIf { it.isNotBlank() },
            baseInput.mapNotNull { it.value.text('-').takeIf { s -> s.isNotBlank() } }.joinToString("\n")
                .takeIf { it.isNotBlank() }
        ).joinToString("\n").prependIndent(" ".repeat(indent))

        return listOf(
            block,
            inboundConnections.joinToString("\n") { it.supplier.text(indent + CHAIN_TEXT_INDENT) }
        ).joinToString("\n")
    }
}