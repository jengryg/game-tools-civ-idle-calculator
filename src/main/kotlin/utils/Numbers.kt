package utils

import java.text.DecimalFormat

fun nf(number: Any): String {
    val dec = DecimalFormat("#,###")

    return dec.format(number)
}