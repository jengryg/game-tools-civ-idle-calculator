package analysis.defs

import DEFAULT_OUTPUT_PATH
import game.model.Model
import utils.io.FileIo
import utils.nf

class ResourcePriceListExporter(
    private val model: Model
) {
    fun export() {
        val table = model.resources.filterValues { it.canPrice && it.canStore }.values.sortedByDescending { it.price }
            .map { r ->
                listOf(r.name, r.price.nf(), r.tier.nf())
            }

        FileIo.writeTable(
            "$DEFAULT_OUTPUT_PATH/defs/resource_price_list.txt",
            table
        )
    }
}