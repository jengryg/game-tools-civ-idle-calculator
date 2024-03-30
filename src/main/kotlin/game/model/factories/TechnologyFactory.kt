package game.model.factories

import game.loader.game.data.TechnologyData
import game.model.game.Age
import game.model.game.Technology

class TechnologyFactory(
    private val ages: Map<String, Age>
) {
    fun process(technologies: Map<String, TechnologyData>): Map<String, Technology> {
        // We need to process the tech tree in sections, to ensure that the predecessor technologies are already available in techTree for referencing.
        // 1. All technologies that do not require another tech.

        processTechTreeSection(technologies.filterValues { it.predecessor.isEmpty() }).forEach { (name, tech) ->
            techTree[name] = tech
        }
        var iterationCounter = 0
        // Iterate until the tech tree is complete.
        while (techTree.size < technologies.size) {
            iterationCounter++

            val nextTechnologies = technologies.filterKeys { !techTree.containsKey(it) }
                .filterValues { it.predecessor.all { rn -> techTree.containsKey(rn.name) } }
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

    private val techTree = mutableMapOf<String, Technology>()

    private fun processTechTreeSection(section: Map<String, TechnologyData>): Map<String, Technology> {
        return section.mapValues { (_, data) -> create(data) }
    }

    private fun create(data: TechnologyData): Technology {
        return Technology(
            name = data.name,
            column = data.column,
            age = ages[data.age.name]!!,
            predecessor = data.predecessor.map { techTree[it.name]!! },
            mods = data.mods
        )
    }
}