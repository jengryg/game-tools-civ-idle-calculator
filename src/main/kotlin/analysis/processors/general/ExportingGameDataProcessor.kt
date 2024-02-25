package analysis.processors.general

import Logging
import OUTPUT_PATH
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import logger
import utils.FileIo
import utils.JsonParser

class ExportingGameDataProcessor(
    private val ap: AnalyserProvider
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    fun exportGameDefinition() {
        FileIo.writeFile("$OUTPUT_PATH/game-definition.json", JsonParser.serialize(ap.gda.gd))
    }

    fun exportPlayerState() {
        FileIo.writeFile("$OUTPUT_PATH/player-state.json", JsonParser.serialize(ap.psa.ps))
    }
}