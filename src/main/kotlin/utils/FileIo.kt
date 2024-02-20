package utils

import Logging
import logger
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream
import kotlin.io.path.readBytes
import kotlin.io.path.readText
import kotlin.io.path.writeText

object FileIo : Logging {
    private val log = logger()

    fun readFile(file: String): String {
        return Paths.get(file).readText(StandardCharsets.UTF_8).also {
            log.atInfo()
                .setMessage("Reading File.")
                .addKeyValue("file") { file }
                .addKeyValue("chars") { it.length }
                .log()
        }
    }

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

    fun writeFile(file: String, content: String?) {
        Paths.get(file).writeText(
            content ?: "",
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING
        )

        log.atInfo()
            .setMessage("Saved File.")
            .addKeyValue("file") { file }
            .log()
    }
}