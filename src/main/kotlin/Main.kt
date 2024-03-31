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
}