package us.timinc.mc.cobblemon.catchondefeat.eventhandlers

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.util.getPlayer
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatMod.config
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.CATCH_ON_DEFEAT
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.DEFEAT_JOIN_CHANCE
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.MUST_BE_SOLOED
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatComponents
import java.util.*
import kotlin.random.Random.Default.nextFloat

object BattleFaintedHandler {
    fun handle(evt: BattleFaintedEvent) {
        val pokemon = evt.killed.effectedPokemon
        if (!evt.battle.isPvW || !pokemon.isWild()) return
        if (!CATCH_ON_DEFEAT.pokemonMatcher(pokemon, true) && !config.everybodysCaughtThisWay) return

        val players = evt.battle.playerUUIDs.mapNotNull(UUID::getPlayer)

        val mustBeSoloed = MUST_BE_SOLOED.pokemonMatcher(pokemon, true)
        if (players.size > 1 && (config.thereCanOnlyBeOnePlayerInBattle || mustBeSoloed)) {
            for (player in players) {
                player.sendSystemMessage(
                    CatchOnDefeatComponents.thereCanOnlyBeOne(pokemon)
                )
            }
            return
        }

        val chance = DEFEAT_JOIN_CHANCE.getValue(pokemon) ?: 100F
        val roll = nextFloat() * 100
        if (roll > chance) {
            for (player in players) {
                player.sendSystemMessage(
                    CatchOnDefeatComponents.ranAway(pokemon)
                )
            }
            return
        }

        val player = players.random()

        val clonedPokemon = pokemon.clone()
        val storage = Cobblemon.storage.getParty(player)
        if (config.heal) clonedPokemon.heal()
        storage.add(clonedPokemon)
        if (config.countsAsCapture) {
            CobblemonEvents.POKEMON_CAPTURED.emit(
                PokemonCapturedEvent(
                    clonedPokemon, player, EmptyPokeBallEntity(
                        PokeBalls.POKE_BALL, player.level()
                    )
                )
            )
        }
        player.sendSystemMessage(
            CatchOnDefeatComponents.joinedTeam(clonedPokemon)
        )
    }
}