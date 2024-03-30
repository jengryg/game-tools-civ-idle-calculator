package game.loader.player.json

class CurrentJson(
    val city: String,
    val unlockedTech: Map<String, Boolean>,
    val greatPeople: Map<String, Int>,
    val tiles: TileWrapper,
    val transportation: TransportationWrapper,
)