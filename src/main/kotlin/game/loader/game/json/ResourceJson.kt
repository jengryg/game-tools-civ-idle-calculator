package game.loader.game.json

import com.fasterxml.jackson.annotation.JsonIgnore

class ResourceJson {
    @JsonIgnore
    var canPrice: Boolean? = null

    @JsonIgnore
    var canStore: Boolean? = null
}