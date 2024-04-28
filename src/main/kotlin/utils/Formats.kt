package utils

import java.text.DecimalFormat

fun Number.nf(): String = DecimalFormat("#,###").format(this)
fun Number.nfd(): String = DecimalFormat("#,###.##").format(this)

fun tabular(table: List<List<String?>>): String {
    val columns = table.maxOf { it.size }
    val columnWidth = (0 until columns).map { c -> table.maxOf { if (it.size <= c) 0 else (it[c]?.length ?: 0) } }

    return table.joinToString("\n") {
        it.mapIndexed { index, s -> (s ?: "").padStart(columnWidth[index]) }.joinToString(" ".repeat(3))
    }
}