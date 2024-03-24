package utils

import java.math.RoundingMode
import java.text.DecimalFormat

fun Int.nf() = DecimalFormat("#,###").format(this)!!
fun Long.nf() = DecimalFormat("#,###").format(this)!!
fun Double.nf(decimals: Int = 0): String =
    DecimalFormat(if (decimals <= 0) "#,###" else "#,###.${"#".repeat(decimals)}").format(this)!!

fun Double.roundHalfUp() = this.toBigDecimal().setScale(0, RoundingMode.HALF_UP).toDouble()