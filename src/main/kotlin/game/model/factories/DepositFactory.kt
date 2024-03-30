package game.model.factories

import game.loader.game.data.DepositData
import game.model.game.Deposit
import game.model.game.Technology

class DepositFactory(
    private val technologies: Map<String, Technology>
) {
    fun process(deposits: Map<String, DepositData>): Map<String, Deposit> {
        return deposits.mapValues { (_, data) -> create(data) }
    }

    fun create(data: DepositData): Deposit {
        return Deposit(
            name = data.name,
            revealedBy = technologies[data.revealedBy.name]!!
        )
    }
}