package utils.io

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class JustNameSerializer : JsonSerializer<HasName>() {
    override fun serialize(value: HasName?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.name.toString())
    }
}