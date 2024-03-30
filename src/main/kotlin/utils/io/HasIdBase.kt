package utils.io

import com.fasterxml.jackson.annotation.JsonKey

open class HasIdBase(
    @JsonKey
    override val id: Int
) : HasId, Comparable<HasIdBase> {
    override fun compareTo(other: HasIdBase): Int {
        return id.compareTo(other.id)
    }
}