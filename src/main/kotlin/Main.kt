import analysis.enrichment.AnalyserProvider
import analysis.processors.chain.ProductionChainProcessor
import analysis.processors.citydata.EnterpriseValueProcessor
import analysis.processors.citymap.CurrentMapProcessor
import analysis.processors.general.BasicInformationProcessor
import ch.qos.logback.classic.Level
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectory
import kotlin.io.path.deleteRecursively

@OptIn(ExperimentalPathApi::class)
fun main(args: Array<String>) {
    setLoggingLevel(Level.TRACE)

    val ap = AnalyserProvider()

    BasicInformationProcessor(ap).apply {
        exportTierBasedEnterpriseValueData()
        exportWonderEnterpriseValueData()
    }

    CurrentMapProcessor(ap).apply {
        createMap()
        export()
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
                exportChain(building = it)
            }
        }

    }
}

