import analysis.defs.BuildingListExporter
import analysis.defs.ResourcePriceListExporter
import analysis.defs.WonderPriceListExporter
import analysis.visual.CurrentMapVisualizer
import ch.qos.logback.classic.Level
import game.loader.DataLoader
import game.model.ModelFactory

fun main(args: Array<String>) {
    setLoggingLevel(Level.TRACE)

    val (gd, pd) = DataLoader().loadCombinedData()
    val model = ModelFactory().create(gd, pd)

    ResourcePriceListExporter(model).apply { export() }
    WonderPriceListExporter(model).apply { export() }
    BuildingListExporter(model).apply { export() }

    CurrentMapVisualizer(model).apply {
        visualize()
        export()
    }

    CurrentMapVisualizer(model).apply {
        export("_tiles")
    }

    // TODO: exporting lists with resources, tiers and prices calculated
    // TODO: exporting lists with cost of wonders and their enterprise value
    // TODO: chain analysis with each variant and for each building
}