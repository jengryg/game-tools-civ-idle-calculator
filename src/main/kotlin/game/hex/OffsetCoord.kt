package game.hex

enum class Offset(val value: Int) {
    EVEN(1),
    ODD(-1)
}

class OffsetCoord(
    val col: Int,
    val row: Int,
) {
    companion object {
        fun roffsetToCube(offset: Offset, h: OffsetCoord): Hex {
            val q = h.col - (h.row + offset.value * (h.row % 2)) / 2;
            val r = h.row;
            val s = -q - r;

            return Hex(q, r, s)
        }
    }

}