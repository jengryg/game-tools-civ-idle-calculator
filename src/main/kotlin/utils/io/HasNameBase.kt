package utils.io

import com.fasterxml.jackson.annotation.JsonKey

open class HasNameBase(
    @JsonKey
    override val name: String
) : HasName, Comparable<HasNameBase> {
    override fun compareTo(other: HasNameBase): Int {
        return name.compareTo(other.name)
    }
}