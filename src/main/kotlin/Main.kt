import ch.qos.logback.classic.Level
import data.DataProvider
import utils.JsonParser

fun main(args: Array<String>) {
    setLoggingLevel(Level.TRACE)

    println(JsonParser.serialize(DataProvider.gs))

    println()

    println(JsonParser.serialize(DataProvider.ps))

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

