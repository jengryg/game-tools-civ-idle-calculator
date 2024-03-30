package utils.io

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class JustIdSerializer : JsonSerializer<HasId>() {
    override fun serialize(value: HasId?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.id.toString())
    }
}