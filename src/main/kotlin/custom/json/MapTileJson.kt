package custom.json

class MapTileJson(
    val id: Int,
    val tile: Int,
    val explored: Boolean,
    val deposit: List<String>,
    val building: TileBuildingJson?
)