package analysis.enrichment

interface IAnalyserProvider {
    val gda: GameDefinitionAnalyser
    val psa: PlayerStateAnalyser
}