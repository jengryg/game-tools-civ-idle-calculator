import analyze.Analyzer
import ch.qos.logback.classic.Level
import custom.CustomDataLoader
import custom.SvgExporter
import game.GameDataLoader
import utils.JsonParser

const val SVG_HEXAGON_SIZE = 64

fun main(args: Array<String>) {
    setLoggingLevel(Level.INFO)

    val gor = GameDataLoader().getRegistry()
    val cor = CustomDataLoader(gor).getRegistry()

//    println(JsonParser.serialize(gor))
//    println(JsonParser.serialize(cor))

    val svgExporter = SvgExporter(gameObjectRegistry = gor, customObjectRegistry = cor)

    svgExporter.drawHexagons()
    svgExporter.drawBuildings()
    svgExporter.export()

    val analyzer = Analyzer(gameObjectRegistry = gor, customObjectRegistry = cor)

    println(JsonParser.serialize(analyzer.analyze()))

}

