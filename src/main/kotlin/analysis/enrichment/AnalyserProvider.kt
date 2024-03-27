package analysis.enrichment

import data.DataProvider

/**
 * Instantiating this class provides the [GameDefinitionAnalyser] and the [PlayerStateAnalyser].
 * Do not instantiate multiple instances. Do not change the data stored in this class.
 */
class AnalyserProvider : IAnalyserProvider {
    override val gda: GameDefinitionAnalyser = GameDefinitionAnalyser(DataProvider.gs)
    override val psa: PlayerStateAnalyser = PlayerStateAnalyser(DataProvider.ps, gda)
}