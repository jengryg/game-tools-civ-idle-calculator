package utils

import Logging
import logger
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream
import kotlin.io.path.readBytes
import kotlin.io.path.readText

object FileIo : Logging {
    private val log = logger()

    fun readFile(file: String): String {
        return Paths.get(file).readText(StandardCharsets.UTF_8).also {
            log.atDebug()
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
                    log.atDebug()
                        .setMessage("Reading compressed File.")
                        .addKeyValue("file") { file }
                        .addKeyValue("chars") { it.length }
                        .log()
                }
            }
        }
    }
}