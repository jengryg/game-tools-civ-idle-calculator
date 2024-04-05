package utils

import java.text.DecimalFormat

fun Number.nf(): String = DecimalFormat("#,###").format(this)
