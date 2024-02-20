package analysis.enrichment

import Logging
import common.BuildingType
import data.definitions.GameDefinition
import data.definitions.IGameDefinition
import data.definitions.model.Building
import logger
import utils.nf

class GameDefinitionAnalyser(
    private val gd: GameDefinition
) : IGameDefinition by gd, Logging {
    private val log = logger()

    val producers: Map<String, Map<String, Building>> = resources.values.associate { resource ->
        resource.name to buildings.filter { bld -> bld.value.output.containsKey(resource.name) }
    }

    val tierBasedEv: Map<Int, Map<String, String>> = buildings.values.filter { it.special == BuildingType.NORMAL }
        .mapNotNull { it.tier }.distinct().associateWith { tier ->
            buildings.filter { it.value.tier == tier && it.value.special == BuildingType.NORMAL }.values.associate {
                it.name to
                        it.getCostForUpgradingLevelsFromTo(
                            0,
                            1
                        ).values.sumOf { ra -> ra.enterpriseValue() }.nf()
            }
        }

    val wonderBasedEv: Map<String, String> =
        buildings.values.filter { it.special == BuildingType.WORLD_WONDER }.associate {
            it.name to it.getCostForUpgradingLevelsFromTo(0, 1).values.sumOf { ra -> ra.enterpriseValue() }.nf()
        }

//    fun exportToJson() {
//        FileIo.writeFile(
//            file = "output/age.json",
//            content = JsonParser.serialize(ages)
//        )
//
//        FileIo.writeFile(
//            file = "output/deposits.json",
//            content = JsonParser.serialize(deposits)
//        )
//
//        FileIo.writeFile(
//            file = "output/resources.json",
//            content = JsonParser.serialize(resources)
//        )
//
//        FileIo.writeFile(
//            file = "output/buildings.json",
//            content = JsonParser.serialize(buildings)
//        )
//
//        FileIo.writeFile(
//            file = "output/technologies.json",
//            content = JsonParser.serialize(technologies)
//        )
//
//        FileIo.writeFile(
//            file = "output/cities.json",
//            content = JsonParser.serialize(cities)
//        )
//        FileIo.writeFile(
//            file = "output/persons.json",
//            content = JsonParser.serialize(persons)
//        )
//        FileIo.writeFile(
//            file = "output/producers.json",
//            content = JsonParser.serialize(producers)
//        )
//
//        FileIo.writeFile(
//            file = "output/tier-base-ev.json",
//            content = JsonParser.serialize(tierBasedEv)
//        )
//        FileIo.writeFile(
//            file = "output/wonder-base-ev.json",
//            content = JsonParser.serialize(wonderBasedEv)
//        )
//    }


}