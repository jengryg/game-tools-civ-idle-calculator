package analysis.processors.chain

import Logging
import OUTPUT_PATH
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import data.definitions.model.Building
import data.definitions.model.Technology
import data.definitions.model.Wonder
import data.player.model.ActiveGreatPerson
import logger
import utils.FileIo
import utils.JsonParser
import utils.stUs
import kotlin.math.ceil
import kotlin.math.roundToLong

class ProductionChainProcessor(
    private val ap: AnalyserProvider
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    val nodes = mutableListOf<ChainNode>()
    val links = mutableListOf<ChainLink>()

    private var nodeCounter = 0
    private fun nextId(): Int {
        return nodeCounter++
    }

    fun exportChain(building: Building, alpMulti: Double = 0.0) {
        val node = getChain(building, alpMulti)

        val chainName = building.name.stUs()

        log.atDebug()
            .setMessage("Exporting Chain Data.")
            .addKeyValue("building") { building.name }
            .log()

        FileIo.writeFile(
            "$OUTPUT_PATH/chains/$chainName/$chainName-alps-${alpMulti}.txt",
            node.text(0)
        )

        val requiredBuildings = node.requiredBuildings().let { map ->
            map.keys.sortedBy { b ->
                ap.gda.buildings[b]!!.tier!!
            }.associateWith { map[it] }
        }

        FileIo.writeFile(
            "$OUTPUT_PATH/chains/$chainName/$chainName-alps-${alpMulti}-req-buildings.json",
            JsonParser.serialize(requiredBuildings)
        )
        FileIo.writeFile(
            "$OUTPUT_PATH/chains/$chainName/$chainName-alps-${alpMulti}-req-buildings.txt",
            requiredBuildings.map { "${it.key};${it.value}" }.joinToString("\n")
        )
    }

    fun getChain(building: Building, alpMulti: Double = 0.0): ChainNode {
        nodeCounter = 0

        val root = initializeNodes(building, alpMulti)

        return root
    }

    private fun createNode(building: Building, alpMulti: Double): ChainNode {
        return ChainNode(
            id = nextId(),
            building = building,
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
}