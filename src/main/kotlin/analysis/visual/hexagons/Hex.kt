package analysis.visual.hexagons

class Hex(val q: Int, val r: Int, val s: Int) {
    init {
        require(q + r + s == 0) { "Hex does not satisfy: q + r + s == 0" }
    }
}