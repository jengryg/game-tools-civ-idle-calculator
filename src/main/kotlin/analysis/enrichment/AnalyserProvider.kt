package analysis.enrichment

import data.DataProvider

class AnalyserProvider : IAnalyserProvider {
    override val gda: GameDefinitionAnalyser = GameDefinitionAnalyser(DataProvider.gs)
    override val psa: PlayerStateAnalyser = PlayerStateAnalyser(DataProvider.ps)
}