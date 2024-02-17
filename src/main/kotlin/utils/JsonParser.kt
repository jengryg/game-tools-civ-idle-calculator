package utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object JsonParser {
    val mapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configure(SerializationFeature.INDENT_OUTPUT, true)
        configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
    }

    inline fun <reified T> deserialize(json: String): T {
        return mapper.readValue<T>(json)
    }

    inline fun <reified T> serialize(data: T): String? {
        return mapper.writeValueAsString(data)
    }
}