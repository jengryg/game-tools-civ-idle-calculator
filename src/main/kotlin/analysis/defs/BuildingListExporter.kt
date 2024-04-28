package analysis.defs

import DEFAULT_OUTPUT_PATH
import game.common.BuildingType
import game.model.Model
import game.model.game.Resource
import utils.io.FileIo
import utils.nf
import utils.nfd

class BuildingListExporter(
    private val model: Model
) {
    fun export() {
        val table = model.buildings.filterValues { it.type == BuildingType.NORMAL }.values.sortedByDescending {
            it.tier
        }.map { b ->
            listOf(b.name).plus(b.tier.nf()).plus(b.unlockedBy?.age?.name).plus(b.ageTierDiff.nf()).plus(
                Resource.getEvOf(b.output).nf()
            ).plus(
                b.outputMulti.nfd()
            )
        }

        FileIo.writeTable(
            "$DEFAULT_OUTPUT_PATH/defs/producers.txt",
            table
        )
    }
}