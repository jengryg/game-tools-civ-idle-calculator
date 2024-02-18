package chain

import CHAIN_TEXT_INDENT
import custom.data.ActiveGreatPerson
import game.data.BoostType
import game.data.Building
import org.slf4j.spi.LoggingEventBuilder

class ChainNode(
    val id: Int = 0,
    val building: Building,
    val affectedBy: List<ActiveGreatPerson>,
) {
    val baseInput: Map<String, ChainNodeIO> = building.input.map { (_, ra) ->
        ra.resource.name to ChainNodeIO(
            resource = ra.resource,
            rawAmount = ra.amount,
            effects = affectedBy.filter { it.person.stdBoost?.any { b -> b.boostType == BoostType.INPUT } == true }
                .associate { it.person.name to (it.level * it.person.value) }
        )
    }.toMap()

    val baseOutput: Map<String, ChainNodeIO> = building.output.map { (_, ra) ->
        ra.resource.name to ChainNodeIO(
            resource = ra.resource,
            rawAmount = ra.amount,
            effects = affectedBy.filter { it.person.stdBoost?.any { b -> b.boostType == BoostType.OUTPUT } == true }
                .associate { it.person.name to (it.level * it.person.value) }
        )
    }.toMap()

    val inboundConnections = mutableListOf<ChainLink>()
    val outboundConnections = mutableListOf<ChainLink>()

    var count: Double = 0.0

    fun logging(builder: LoggingEventBuilder): LoggingEventBuilder {
        return builder
            .addKeyValue("ChainNode#Id") { id }
            .addKeyValue("ChainNode#building") { building.name }
            .addKeyValue("ChainNode#affectedBy") { affectedBy.map { "${it.person.name} ${it.level}" } }
            .addKeyValue("ChainNode#baseInput") {
                baseInput.values.map { it }.toTypedArray().contentToString()
            }
            .addKeyValue("ChainNode#baseOutput") {
                baseOutput.values.map { it }.toTypedArray().contentToString()
            }
            .addKeyValue("ChainNode#count") { count }
    }

    fun text(indent: Int): String {
        return mutableListOf(
            """
            *----------------------------------------------------------------------------------------------------
            |   ${building.name}: $count
            |   ${affectedBy.map { "${it.person.name} ${it.level}" }}
            | + ${baseOutput.values.map { it }.toTypedArray().contentToString()}
            | - ${baseInput.values.map { it }.toTypedArray().contentToString()}
            """.trimIndent().prependIndent(" ".repeat(indent))
        ).plus(inboundConnections.map { it.supplier.text(indent + CHAIN_TEXT_INDENT) }).joinToString("\n")
    }
}