package game.loader.game.converter

import Logging
import game.loader.game.data.AgeData
import game.loader.game.data.TechnologyData
import game.loader.game.json.TechJson
import logger

class TechnologyConverter(
    ages: Map<String, AgeData>,
) : Logging {
    private val log = logger()

    private val ageByCol = ages.flatMap { (_, age) ->
        age.cols.map { it to age }
    }.toMap()

    private val techTree = mutableMapOf<String, TechnologyData>()

    fun process(technologies: Map<String, TechJson>): Map<String, TechnologyData> {
        techTree.clear() // Ensure that the tree is empty when we start.

        // We need to process the tech tree in sections, to ensure that the predecessor technologies are already available in techTree for referencing.
        // 1. All technologies that do not require another tech.
        processTechTreeSection(technologies.filterValues { it.requireTech.isNullOrEmpty() }).forEach { (name, tech) ->
            techTree[name] = tech
        }
        var iterationCounter = 0
        // Iterate until the tech tree is complete.
        while (techTree.size < technologies.size) {
            iterationCounter++

            val nextTechnologies = technologies.filterKeys { !techTree.containsKey(it) }
                .filterValues { it.requireTech?.all { rn -> techTree.containsKey(rn) } ?: true }
            // no double processing, only select techs where all requirements are already done

            processTechTreeSection(nextTechnologies).forEach { (name, tech) ->
                techTree[name] = tech
            }

            if (iterationCounter > technologies.size) {
                throw IllegalStateException("Processing tech tree exceeded iteration limit: Check the tree for isolated components!")
            }
        }

        return techTree.toMap()
    }

    private fun processTechTreeSection(section: Map<String, TechJson>): Map<String, TechnologyData> {
        return section.mapValues { (name, json) -> create(name, json) }.also {
            log.atDebug()
                .setMessage("Technology tree construction section finished.")
                .addKeyValue("size") { it.size }
                .log()
        }
    }

    private fun create(name: String, json: TechJson): TechnologyData {
        return TechnologyData(
            name = name,
            column = json.column,
            age = ageByCol[json.column]!!,
            predecessor = json.requireTech?.map { techTree[it]!! } ?: emptyList(),
            revealDepositNames = json.revealDeposit ?: emptyList(),
            unlocksBuildingNames = json.unlockBuilding ?: emptyList(),
            buildingMultipliers = json.buildingMultiplier?.flatMap { (bName, multi) ->
                multi.map { (type, factor) -> Triple(bName, type, factor.toDouble()) }
            } ?: emptyList()
        )
    }
}