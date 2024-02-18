import analyze.Analyzer
import ch.qos.logback.classic.Level
import custom.CustomDataLoader
import custom.SvgExporter
import game.GameDataLoader
import game.data.BuildingType
import utils.JsonParser

fun main(args: Array<String>) {
    setLoggingLevel(Level.INFO)

    val gor = GameDataLoader().getRegistry().apply {
        exportToJson()
    }
    val cor = CustomDataLoader(gor).getRegistry()

    val svgExporter = SvgExporter(gameObjectRegistry = gor, customObjectRegistry = cor)

    svgExporter.drawHexagons()
    svgExporter.drawBuildings()
    svgExporter.export()

    val analyzer = Analyzer(gameObjectRegistry = gor, customObjectRegistry = cor)

    println(JsonParser.serialize(analyzer.analyze()))
}

