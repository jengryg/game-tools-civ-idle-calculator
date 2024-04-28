package analysis.defs

import constants.DEFAULT_OUTPUT_PATH
import game.model.Model
import utils.io.FileIo
import utils.io.HasName
import utils.toConstName

class KotlinConstantsExporter(
    private val model: Model
) {
    fun export() {
        exportBuildings()
        exportResources()
    }

    private fun exportBuildings() {
        FileIo.writeFile(
            "$DEFAULT_OUTPUT_PATH/consts/buildings_constants.txt",
            toTable(model.buildings.values.sortedBy { it.name }.toList())
        )
    }

    private fun exportResources() {
        FileIo.writeFile(
            "$DEFAULT_OUTPUT_PATH/consts/resources_constants.txt",
            toTable(model.resources.values.sortedBy { it.name }.toList())
        )
    }

    private fun toTable(elements: List<HasName>): String {
        return elements.map { it.name }.joinToString("\n") { "const val ${it.toConstName()} = \"$it\"" }
    }
}