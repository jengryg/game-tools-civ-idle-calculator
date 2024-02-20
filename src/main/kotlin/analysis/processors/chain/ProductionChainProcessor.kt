package analysis.processors.chain

import Logging
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import data.definitions.model.Building
import data.definitions.model.Wonder
import data.player.model.ActiveGreatPerson
import logger
import kotlin.math.ceil
import kotlin.math.roundToLong

class ProductionChainProcessor(
    ap: AnalyserProvider
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    val nodes = mutableListOf<ChainNode>()
    val links = mutableListOf<ChainLink>()

    private var nodeCounter = 0
    private fun nextId(): Int {
        return nodeCounter++
    }

    fun getChain(building: Building): ChainNode {
        nodeCounter = 0

        val root = initializeNodes(building)

        return root
    }

    private fun createNode(building: Building): ChainNode {
        return ChainNode(
            id = nextId(),
            building = building,
            affectedBy = getGreatPersonsAffecting(building),
            applyWonders = getWondersAffecting(building)
        ).also {
            nodes.add(it)
        }
    }

    private fun initializeNodes(building: Building): ChainNode {
        val root = createNode(building)
        log.atDebug()
            .setMessage("Root created.")
            .also {
                root.logging(it)
            }
            .log()

        root.count = 1.0

        processInputs(root)

        return root
    }

    private fun processInputs(node: ChainNode) {
        node.baseInput.forEach { (rName, input) ->
            // TODO: selecting the producer here or creating alternatives if multiple possibilities are available
            val producer = gda.producers[rName]?.values?.first()
                ?: throw IllegalArgumentException("No producer found for $rName.")

            val supplier = createNode(producer)

            val link = ChainLink(nextId(), supplier, node, input.resource).also { links.add(it) }
            node.inboundConnections.add(link)
            supplier.outboundConnections.add(link)

            supplier.count =
                (node.count * input.effectiveAmount) / supplier.baseOutput[rName]!!.effectiveAmount

            link.amount = ceil(supplier.count * supplier.baseOutput[rName]!!.effectiveAmount).roundToLong()

            log.atDebug()
                .setMessage("Supplier node created and linked.")
                .also {
                    supplier.logging(it)
                    link.logging(it)
                }
                .log()

            processInputs(supplier)
        }
    }

    private fun getGreatPersonsAffecting(building: Building): List<ActiveGreatPerson> {
        return psa.greatPeople.filter {
            it.value.person.stdBoost?.any { b -> b.boostTarget == building } == true
        }.values.toList()
    }

    private fun getWondersAffecting(building: Building): List<Wonder> {
        return psa.activeWonders.filter {
            it.value.stdBoost?.any { b -> b.boostTarget == building } == true
        }.values.toList()
    }
}