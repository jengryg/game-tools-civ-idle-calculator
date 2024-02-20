package chain

import data.definitions.model.Resource
import org.slf4j.spi.LoggingEventBuilder
import utils.nf

class ChainLink(
    val id: Int,
    val supplier: ChainNode,
    val consumer: ChainNode,
    val resource: Resource,
) {
    var amount: Long = 0L

    fun logging(builder: LoggingEventBuilder): LoggingEventBuilder {
        return builder
            .addKeyValue("ChainLink#Id") { id }
            .addKeyValue("supplier#Id") { supplier.id }
            .addKeyValue("consumer#Id") { consumer.id }
            .addKeyValue("resource") { resource.name }
            .addKeyValue("amount") { amount.nf() }
    }
}