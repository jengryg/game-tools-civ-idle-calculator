package analysis.defs

import DEFAULT_OUTPUT_PATH
import game.common.BuildingType
import game.model.Model
import game.model.game.Resource
import utils.io.FileIo
import utils.nf

class WonderPriceListExporter(
    private val model: Model
) {
    fun export() {
        val table = model.buildings.filterValues { it.type == BuildingType.WORLD_WONDER }.values.sortedByDescending {
            Resource.getEvOf(it.cost)
        }.map { b ->
            listOf(b.name).plus(Resource.getEvOf(b.cost).nf()).plus(
                b.cost.map { (r, a) -> "${a.nf()} ${r.name}" }
            )
        }

        FileIo.writeTable(
            "$DEFAULT_OUTPUT_PATH/defs/wonder_price.txt",
            table
        )
    }
}