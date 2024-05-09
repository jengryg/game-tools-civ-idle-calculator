package analysis.current

import constants.DEFAULT_OUTPUT_PATH
import game.common.BuildingType
import game.model.Model
import utils.io.FileIo
import utils.nf
import utils.nfd

class EffectiveModifierExporter(
    private val model: Model
) {
    fun export() {
        val header = listOf(
            listOf("Name", "Tier", "Output Multi", "Input Multi"),
            listOf("", "Effect", "Type", "Source")
        )
        val content = model.buildings.filterValues { it.type == BuildingType.NORMAL }.values.sortedByDescending {
            it.tier
        }.flatMap { b ->

            listOf(
                listOf(b.name, b.tier.nf(), (1.0 + b.outputMulti).nfd(), (1.0 + b.inputMulti).nfd())
            ).plus(
                b.outputMods.map { listOf("OUTPUT", it.effect.nfd(), it.mod.type.toString(), it.mod.from) }
            ).plus(
                b.inputMods.map { listOf("INPUT", it.effect.nfd(), it.mod.type.toString(), it.mod.from) }
            ).plus(
                listOf(listOf(), listOf())
            )
        }.toList()

        val table = header.plus(content)

        FileIo.writeTable(
            "$DEFAULT_OUTPUT_PATH/current/modifiers.txt",
            table
        )
    }
}