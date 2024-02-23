package analysis.strategy

import Logging
import OUTPUT_PATH
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import logger
import utils.FileIo
import utils.nf

class GrottoStrategy(
    private val ap: AnalyserProvider
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    fun createSimpleStrategy(desiredLevel: Int) {

        val oilWell = ap.gda.buildings["OilWell"]!!
        val gasWell = ap.gda.buildings["NaturalGasWell"]!!

        val oilWellCost = oilWell.getCostForUpgradingLevelsFromTo(0, desiredLevel)
        val gasWellCost = gasWell.getCostForUpgradingLevelsFromTo(0, desiredLevel)

        val oilWellValue = oilWellCost.values.sumOf { it.enterpriseValue() }
        val gasWellValue = gasWellCost.values.sumOf { it.enterpriseValue() }

        val oilWellCostGrotto = oilWell.getCostForUpgradingLevelsFromTo(0, desiredLevel + 5)
        val gasWellCostGrotto = gasWell.getCostForUpgradingLevelsFromTo(0, desiredLevel + 5)

        val oilWellValueGrotto = oilWellCostGrotto.values.sumOf { it.enterpriseValue() }
        val gasWellValueGrotto = gasWellCostGrotto.values.sumOf { it.enterpriseValue() }

        val header =
            """
            Simple Grotto Strategy using $desiredLevel OilWell/GasWell
            ===========================${"=".repeat(28)}
            """.trimIndent()

        val costOilWell =
            oilWellCost.map { "${it.value.resource.name.padEnd(26, ' ')}:${it.value.amount.nf().padStart(28, ' ')}" }.joinToString("\n")


        val costGasWell =
            gasWellCost.map { "${it.value.resource.name.padEnd(26, ' ')}:${it.value.amount.nf().padStart(28, ' ')}" }.joinToString("\n")


        val oilCostHeader =
            """
            Building Oil Well $desiredLevel cost:
            """.trimIndent()

        val gasCostHeader =
            """
            Building Gas Well $desiredLevel cost:
            """.trimIndent()

        val result =
            """
            ===========================${"=".repeat(28)}
            Oil Well Value:            ${oilWellValue.nf().padStart(28, ' ')}
            Gas Well Value:            ${gasWellValue.nf().padStart(28, ' ')}
            ===========================${"=".repeat(28)}
            Activating Grotto adds 5 levels to each instance:
            ===========================${"=".repeat(28)}
            Oil Well Value Grotto:     ${oilWellValueGrotto.nf().padStart(28, ' ')}
            Gas Well Value Grotto:     ${gasWellValueGrotto.nf().padStart(28, ' ')}
            ===========================${"=".repeat(28)}
            Grotto Impact Oil:         ${(oilWellValueGrotto - oilWellValue).nf().padStart(28, ' ')}
            Relative:                  ${(100 * oilWellValueGrotto/oilWellValue).nf().plus(" %").padStart(28, ' ')}
            Grotto Impact Gas:         ${(gasWellValueGrotto - gasWellValue).nf().padStart(28, ' ')}
            Relative:                  ${(100 * gasWellValueGrotto/gasWellValue).nf().plus(" %").padStart(28, ' ')}
            ===========================${"=".repeat(28)}
            """.trimIndent()


        FileIo.writeFile(
            "$OUTPUT_PATH/strategy/grotto/grotto-simple-$desiredLevel.txt",
            listOf(
                header,
                oilCostHeader,
                costOilWell,
                gasCostHeader,
                costGasWell,
                result
            ).joinToString("\n")
        )
    }
}