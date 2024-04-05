package utils.io

import Logging
import logger
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream
import kotlin.io.path.createParentDirectories
import kotlin.io.path.readBytes
import kotlin.io.path.readText
import kotlin.io.path.writeText

object FileIo : Logging {
    private val log = logger()

    /**
     * @param file path and name of the file to read
     */
    fun readFile(file: String): String {
        return Paths.get(file).readText(StandardCharsets.UTF_8).also {
            log.atInfo()
                .setMessage("Reading File.")
                .addKeyValue("file") { file }
                .addKeyValue("chars") { it.length }
                .log()
        }
    }

    /**
     * @param file path and name of the file to read and decompress
     */
    fun readCompressedFile(file: String): String {
        return Paths.get(file).readBytes().let { raw ->
            (byteArrayOf().plus(raw)).inputStream()
        }.let { inStream ->
            InflaterInputStream(inStream, Inflater(true)).bufferedReader().use { reader ->
                reader.readText().also {
                    log.atInfo()
                        .setMessage("Reading compressed File.")
                        .addKeyValue("file") { file }
                        .addKeyValue("chars") { it.length }
                        .log()
                }
            }
        }
    }

    /**
     * @param file path and name of the file to create with extension
     * @param content the content to write into the file
     */
    fun writeFile(file: String, content: String?) {
        Paths.get(file).createParentDirectories().writeText(
            content ?: "",
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING,
        )

        log.atInfo()
            .setMessage("Saved File.")
            .addKeyValue("file") { file }
            .log()
    }

    /**
     * @param file path and name of the file to create with extension
     * @param table the table given as list of rows, where each row is a list of cells to write into the file
     */
    fun writeTable(file: String, table: List<List<String?>>) {
        val columns = table.maxOf { it.size }
        val columnWidth = (0 until columns).map { c -> table.maxOf { if (it.size <= c) 0 else (it[c]?.length ?: 0) } }

        Paths.get(file).createParentDirectories().writeText(
            table.joinToString("\n") {
                it.mapIndexed { index, s -> (s ?: "").padStart(columnWidth[index]) }.joinToString(" ".repeat(3))
            },
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING,
        )

        log.atInfo()
            .setMessage("Saved File.")
            .addKeyValue("file") { file }
            .log()
    }
}