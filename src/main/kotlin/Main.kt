import analysis.visual.CurrentMapVisualizer
import ch.qos.logback.classic.Level
import game.loader.DataLoader
import game.model.ModelFactory

fun main(args: Array<String>) {
    setLoggingLevel(Level.TRACE)

    val (gd, pd) = DataLoader().loadCombinedData()
    val model = ModelFactory().create(gd, pd)

    CurrentMapVisualizer(model).apply {
        visualize()
        export()
    }

    // TODO: exporting lists with resources, tiers and prices calculated
    // TODO: exporting lists with cost of wonders and their enterprise value
    // TODO: chain analysis with each variant and for each building
}