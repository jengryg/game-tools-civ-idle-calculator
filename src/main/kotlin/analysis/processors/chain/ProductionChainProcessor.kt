package analysis.processors.chain

import Logging
import OUTPUT_PATH
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import data.definitions.model.Building
import data.definitions.model.Wonder
import data.player.model.ActiveGreatPerson
import logger
import utils.FileIo
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

    fun exportChain(building: Building, alpMulti: Double) {
        val node = getChain(building, alpMulti)
        FileIo.writeFile(
            "$OUTPUT_PATH/chains/prod-chain-${building.name.replace(" ", "_")}-alps-${alpMulti}.txt",
            node.text(0)
        )
    }

    fun getChain(building: Building, alpMulti: Double): ChainNode {
        nodeCounter = 0

        val root = initializeNodes(building, alpMulti)

        return root
    }

    private fun createNode(building: Building, alpMulti: Double): ChainNode {
        return ChainNode(
            id = nextId(),
            building = building,
            affectedBy = getGreatPersonsAffecting(building),
            applyWonders = getWondersAffecting(building),
            alpMulti = alpMulti
        ).also {
            nodes.add(it)
        }
    }

    private fun initializeNodes(building: Building, alpMulti: Double): ChainNode {
        val root = createNode(building, alpMulti)
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

            val supplier = createNode(producer, node.alpMulti)

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