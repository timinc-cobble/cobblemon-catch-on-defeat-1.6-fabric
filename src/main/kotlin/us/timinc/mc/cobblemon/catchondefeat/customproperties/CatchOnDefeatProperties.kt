package us.timinc.mc.cobblemon.catchondefeat.customproperties

import com.cobblemon.mod.common.api.properties.CustomPokemonProperty

object CatchOnDefeatProperties {
    val CATCH_ON_DEFEAT = CatchOnDefeatProperty()

    fun register() {
        CustomPokemonProperty.register(CATCH_ON_DEFEAT)
    }
}