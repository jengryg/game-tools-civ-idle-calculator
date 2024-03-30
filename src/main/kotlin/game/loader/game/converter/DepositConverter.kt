package game.loader.game.converter

import game.loader.game.data.DepositData
import game.loader.game.data.TechnologyData

class DepositConverter(
    private val technologies: Map<String, TechnologyData>
) {
    fun process(deposits: Map<String, Boolean>): Map<String, DepositData> {
        return deposits.mapValues { (name, _) -> create(name) }
    }

    private fun create(name: String): DepositData {
        return DepositData(
            name = name,
            revealedBy = getUnlockTechnology(name)
        )
    }

    private fun getUnlockTechnology(depositName: String): TechnologyData {
        return technologies.values.filter { it.revealDepositNames.contains(depositName) }.also {
            if (it.size > 1) {
                throw IllegalStateException("Deposit $depositName is unlocked by more than one technology $it!")
            }
            if (it.isEmpty()) {
                throw IllegalStateException("Deposit $depositName is not unlocked by any technology $it!")
            }
        }.first()
    }
}