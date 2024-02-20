package analysis.enrichment

import analysis.enrichment.GameDefinitionAnalyser
import analysis.enrichment.PlayerStateAnalyser

interface IAnalyserProvider {
    val gda: GameDefinitionAnalyser
    val psa: PlayerStateAnalyser
}