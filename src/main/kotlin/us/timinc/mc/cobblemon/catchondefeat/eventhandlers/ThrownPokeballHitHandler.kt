package us.timinc.mc.cobblemon.catchondefeat.eventhandlers

import com.cobblemon.mod.common.api.events.pokeball.ThrownPokeballHitEvent
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatMod.config
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatComponents

object ThrownPokeballHitHandler {
    fun handle(evt: ThrownPokeballHitEvent) {
        val pokemon = evt.pokemon.pokemon
        if (config.preventRegularCapture && (CatchOnDefeatProperties.CATCH_ON_DEFEAT.pokemonMatcher(
                pokemon,
                true
            ) || config.everybodysCaughtThisWay)
        ) {
            (evt.pokeBall.owner as? ServerPlayer)?.sendSystemMessage(CatchOnDefeatComponents.cantCatch(pokemon))
            evt.cancel()
        }
    }
}