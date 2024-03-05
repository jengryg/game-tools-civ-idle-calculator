import analysis.enrichment.AnalyserProvider
import analysis.processors.chain.ProductionChainProcessor
import analysis.processors.citydata.EnterpriseValueProcessor
import analysis.processors.citydata.EnterpriseValueProcessorLegacy
import analysis.processors.citymap.CurrentMapProcessor
import analysis.processors.general.BasicInformationProcessor
import analysis.processors.general.ExportingAnalyserProviderData
import analysis.strategy.GrottoStrategy
import ch.qos.logback.classic.Level
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteRecursively

@OptIn(ExperimentalPathApi::class)
fun main(args: Array<String>) {
    setLoggingLevel(Level.TRACE)

    val ap = AnalyserProvider()

    BasicInformationProcessor(ap).apply {
        exportResourceList()
        exportBuildingResourceNeeds()
        exportWonderResourceNeeds()
        exportTierBasedEnterpriseValueData()
        exportWonderEnterpriseValueData()
    }

    CurrentMapProcessor(ap).apply {
        createMap()
        export()
    }

    EnterpriseValueProcessorLegacy(ap).apply {
        createReport()
    }

    EnterpriseValueProcessor(ap).apply {
        createReport()
    }

    ProductionChainProcessor(ap).apply {
        Paths.get("$OUTPUT_PATH/chains").apply {
            deleteRecursively()
            createDirectory()
        }

        ap.gda.buildings.values.forEach {
            if (it.input.isNotEmpty()) {
                exportChain(building = it, alpMulti = 0.0)
                exportChain(building = it, alpMulti = 1.0)
                exportChain(building = it, alpMulti = 2.0)
                exportChain(building = it, alpMulti = 3.0)
                exportChain(building = it, alpMulti = 4.0)
            }
        }
    }

    GrottoStrategy(ap).apply {
        Paths.get("$OUTPUT_PATH/strategy/grotto").apply {
            deleteRecursively()
            createDirectories()
        }
        (20..40).forEach {
            createSimpleStrategy(it)
        }
    }

    ExportingAnalyserProviderData(ap).apply {
        exportGameDefinition()
        exportPlayerState()
    }
}