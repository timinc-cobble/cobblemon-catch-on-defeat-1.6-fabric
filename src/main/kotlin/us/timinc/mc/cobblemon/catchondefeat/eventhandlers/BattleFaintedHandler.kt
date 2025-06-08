package us.timinc.mc.cobblemon.catchondefeat.eventhandlers

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.util.getPlayer
import net.minecraft.network.chat.Component
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatMod.config
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.CATCH_ON_DEFEAT
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.DEFEAT_JOIN_CHANCE
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.MUST_BE_SOLOED
import java.util.*
import kotlin.random.Random.Default.nextFloat

object BattleFaintedHandler {
    fun handle(evt: BattleFaintedEvent) {
        val pokemon = evt.killed.effectedPokemon.clone()
        if (!evt.battle.isPvW || !pokemon.isWild()) return
        if (!CATCH_ON_DEFEAT.pokemonMatcher(pokemon, true) && !config.everybodysCaughtThisWay) return

        val players = evt.battle.playerUUIDs.mapNotNull(UUID::getPlayer)

        val mustBeSoloed = MUST_BE_SOLOED.pokemonMatcher(pokemon, true)
        if (players.size > 1 && (config.thereCanOnlyBeOnePlayerInBattle || mustBeSoloed)) {
            for (player in players) {
                player.sendSystemMessage(
                    Component.translatable(
                        "catch_on_defeat.feedback.there_can_only_be_one",
                        pokemon.getDisplayName()
                    )
                )
            }
            return
        }

        val chance = DEFEAT_JOIN_CHANCE.getValue(pokemon) ?: 100F
        val roll = nextFloat() * 100
        if (roll > chance) {
            for (player in players) {
                player.sendSystemMessage(
                    Component.translatable(
                        "catch_on_defeat.feedback.ran_away",
                        pokemon.getDisplayName()
                    )
                )
            }
            return
        }

        val player = players.random()

        val storage = Cobblemon.storage.getParty(player)
        if (config.heal) pokemon.heal()
        storage.add(pokemon)
        player.sendSystemMessage(
            Component.translatable(
                "catch_on_defeat.feedback.joined_team",
                pokemon.getDisplayName()
            )
        )
    }
}