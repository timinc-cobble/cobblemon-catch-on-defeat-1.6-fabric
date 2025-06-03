package us.timinc.mc.cobblemon.catchondefeat.customproperties

import com.cobblemon.mod.common.api.properties.CustomPokemonProperty

object CatchOnDefeatProperties {
    val CATCH_ON_DEFEAT = CatchOnDefeatProperty()
    val DEFEAT_JOIN_CHANCE = DefeatJoinChanceProperty()
    val MUST_BE_SOLOED = MustBeSoloedProperty()

    fun register() {
        CustomPokemonProperty.register(CATCH_ON_DEFEAT)
        CustomPokemonProperty.register(DEFEAT_JOIN_CHANCE)
        CustomPokemonProperty.register(MUST_BE_SOLOED)
    }
}