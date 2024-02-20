package hexagons

import kotlin.math.sqrt

class Orientation(
    val f0: Double = sqrt(3.0),
    val f1: Double = sqrt(3.0) / 2.0,
    val f2: Double = 0.0,
    val f3: Double = 3.0 / 2.0,
    val b0: Double = sqrt(3.0) / 3.0,
    val b1: Double = -1.0 / 3.0,
    val b2: Double = 0.0,
    val b3: Double = 2.0 / 3.0,
    val start_angle: Double = 0.5,
)