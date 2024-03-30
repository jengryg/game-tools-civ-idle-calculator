package game.loader.player.json

class TileJson(
    val id: Int,
    val tile: Int,
    val explored: Boolean,
    val deposit: List<String>,
    val building: TileBuildingJson?
)