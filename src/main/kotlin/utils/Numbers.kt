package utils

import java.text.DecimalFormat

fun Int.nf() = DecimalFormat("#,###").format(this)!!
fun Long.nf() = DecimalFormat("#,###").format(this)!!
fun Double.nf(decimals: Int = 0): String =
    DecimalFormat(if (decimals <= 0) "#,###" else "#,###.${"#".repeat(decimals)}").format(this)!!