package us.timinc.mc.cobblemon.catchondefeat.eventhandlers

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.util.getPlayer
import net.minecraft.network.chat.Component
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatMod
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties
import java.util.*

object BattleFaintedHandler {
    fun handle(evt: BattleFaintedEvent) {
        val pokemon = evt.killed.effectedPokemon.clone()
        if (!CatchOnDefeatProperties.CATCH_ON_DEFEAT.pokemonMatcher(pokemon, true)) return
        if (!evt.battle.isPvW || !pokemon.isWild()) return
        val players = evt.battle.playerUUIDs.mapNotNull(UUID::getPlayer)
        if (players.size > 1) return
        val player = players.first()
        val storage = Cobblemon.storage.getParty(player)
        if (CatchOnDefeatMod.config.heal) pokemon.heal()
        storage.add(pokemon)
        player.sendSystemMessage(Component.translatable("catch_on_defeat.feedback.joined_team", pokemon.species.translatedName))
    }
}