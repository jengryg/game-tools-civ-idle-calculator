import analysis.current.EffectiveModifierExporter
import analysis.defs.BuildingListExporter
import analysis.defs.KotlinConstantsExporter
import analysis.defs.ResourcePriceListExporter
import analysis.defs.WonderPriceListExporter
import analysis.strategy.SimplexStrategyFactory
import analysis.visual.CurrentBuildingLevelVisualizer
import analysis.visual.CurrentMapVisualizer
import ch.qos.logback.classic.Level
import constants.*
import game.common.modifiers.BuildingModTarget
import game.common.modifiers.BuildingModType
import game.loader.DataLoader
import game.model.ModelFactory

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

    CurrentBuildingLevelVisualizer(model).apply {
        visualize()
        export()
    }

    model.getBuilding(SATELLITE_FACTORY).addCustomMod(
        type = BuildingModType.WONDER,
        target = BuildingModTarget.OUTPUT,
        from = APOLLO_PROGRAM,
        totalEffect = 1.0
    )

//    listOf(BuildingModTarget.OUTPUT, BuildingModTarget.STORAGE, BuildingModTarget.WORKER).forEach { target ->
//        listOf(
//            STEEL_MILL,
//            FRIGATE_BUILDER,
//            SPACECRAFT_FACTORY,
//            BATTLESHIP_BUILDER,
//            BLACKSMITH,
//            PUBLISHING_HOUSE
//        ).forEach { bld ->
//            model.getBuilding(bld).addCustomMod(
//                type = BuildingModType.WONDER,
//                target = target,
//                from = UNITED_NATIONS,
//                totalEffect = 5.0
//            )
//        }
//    }

    listOf(SPACECRAFT_FACTORY, SATELLITE_FACTORY).forEach {
        model.getBuilding(it).addCustomMod(
            type = BuildingModType.GREAT_PERSON,
            target = BuildingModTarget.OUTPUT,
            from = "Doubled GP",
            totalEffect = model.getObtainedGreatPeople("SergeiKorolev").totalEffect
        )
    }

    val ssf = SimplexStrategyFactory(model)

//    model.buildings.filterValues { it.type == BuildingType.NORMAL }.forEach { (name, building) ->
//        val r = Resource.getEvOf(building.effectiveOutput)
//        if (r > 0.0) {
//            ssf.processByEnterpriseValueTarget(name, targetEnterprise).apply {
//                solve()
//                export()
//            }
//        }
//    }

    ssf.process(SPACECRAFT_FACTORY, 200.0).apply {
        solve()
        export("simple")
    }

    ssf.process(SCHOOL, 200.0).apply {
        solve()
        export("simple")
    }

    // TODO: exporting lists with resources, tiers and prices calculated
    // TODO: exporting lists with cost of wonders and their enterprise value
    // TODO: chain analysis with each variant and for each building
}