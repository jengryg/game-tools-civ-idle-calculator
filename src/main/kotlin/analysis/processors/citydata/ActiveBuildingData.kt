package analysis.processors.citydata

import Logging
import analysis.enrichment.AnalyserProvider
import analysis.enrichment.IAnalyserProvider
import logger

class ActiveBuildingData(
    private val ap: AnalyserProvider,
) : IAnalyserProvider by ap, Logging {
    private val log = logger()

    fun createReport() {
        
    }
}