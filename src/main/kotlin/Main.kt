import analysis.enrichment.AnalyserProvider
import analysis.processors.citymap.CurrentMapProcessor
import ch.qos.logback.classic.Level

fun main(args: Array<String>) {
    setLoggingLevel(Level.TRACE)

    val analyserProvider = AnalyserProvider()

    CurrentMapProcessor(analyserProvider).apply {
        createMap()
        export()
    }


//    val svgExporter = SvgExporter(gameDefinitions = gor, playerState = cor)

//    svgExporter.drawHexagons()
//    svgExporter.drawBuildings()
//    svgExporter.export()

//    val analyzer = Analyzer(gameDefinitions = gor, playerState = cor)

//    println(JsonParser.serialize(analyzer.analyze()))

//    val chainFactory = ProductionChainFactory(
//        gor,
//        cor
//    )
//
//    val node = chainFactory.getChain(building = gor.buildings["DynamiteWorkshop"]!!)
//
//    println(node.text(0))
}

