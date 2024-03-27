package analysis.enrichment

import Logging
import data.player.IPlayerState
import data.player.PlayerState
import logger

class PlayerStateAnalyser(
    val ps: PlayerState,
    gda: GameDefinitionAnalyser
) : IPlayerState by ps, Logging {
    private val log = logger()

    init {
        gda.buildings.forEach { (name, bld) ->
            ps.greatPeople.forEach {
                if (it.value.person.stdBoost?.any { b -> b.boostTarget == bld } == true) {
                    bld.affectedByGreatPersons.add(it.value)
                }
            }

            ps.activeWonders.forEach {
                if (it.value.stdBoost?.any { b -> b.boostTarget == bld } == true) {
                    bld.affectedByWonders.add(it.value)
                }
            }

            ps.unlockedTechnology.forEach {
                if (it.value.stdBoost.any { b -> b.boostTarget == bld }) {
                    bld.affectedByTechnologies.add(it.value)
                }
            }
        }
    }
}