import analysis.current.EffectiveModifierExporter
import analysis.defs.BuildingListExporter
import analysis.defs.KotlinConstantsExporter
import analysis.defs.ResourcePriceListExporter
import analysis.defs.WonderPriceListExporter
import analysis.strategy.SimplexStrategyFactory
import analysis.visual.CurrentMapVisualizer
import ch.qos.logback.classic.Level
import constants.SPACECRAFT_FACTORY
import game.common.BuildingType
import game.loader.DataLoader
import game.model.ModelFactory
import game.model.game.Resource

fun main(args: Array<String>) {
    setLoggingLevel(args.toList().getOrElse(0) { Level.WARN.levelStr })

    val (gd, pd) = DataLoader().loadCombinedData()
    val model = ModelFactory().create(gd, pd)

    ResourcePriceListExporter(model).apply { export() }
    WonderPriceListExporter(model).apply { export() }
    BuildingListExporter(model).apply { export() }
    KotlinConstantsExporter(model).apply { export() }
    EffectiveModifierExporter(model).apply { export() }

    CurrentMapVisualizer(model).apply {
        visualize()
        export()
    }

    val ssf = SimplexStrategyFactory(model)
    val targetEnterprise = 2_000_000_000.0 / 20.0

    model.buildings.filterValues { it.type == BuildingType.NORMAL }.forEach { (name, building) ->
        val r = Resource.getEvOf(building.effectiveOutput)
        if (r > 0.0) {
            ssf.processByEnterpriseValueTarget(name, targetEnterprise).apply {
                solve()
                export()
            }
        }
    }

    ssf.process(SPACECRAFT_FACTORY, 200.0).apply {
        solve()
        export("simple")
    }

    // TODO: exporting lists with resources, tiers and prices calculated
    // TODO: exporting lists with cost of wonders and their enterprise value
    // TODO: chain analysis with each variant and for each building
}