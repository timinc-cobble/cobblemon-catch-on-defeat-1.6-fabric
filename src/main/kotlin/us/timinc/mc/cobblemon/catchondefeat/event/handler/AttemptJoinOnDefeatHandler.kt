package us.timinc.mc.cobblemon.catchondefeat.event.handler

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatMod.config
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.CATCH_ON_DEFEAT
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.DEFEAT_JOIN_CHANCE
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.MUST_BE_SOLOED
import us.timinc.mc.cobblemon.catchondefeat.event.JoinDefeatEvent
import us.timinc.mc.cobblemon.catchondefeat.holding.PokemonJoinPartyHolding
import us.timinc.mc.cobblemon.catchondefeat.network.clientbound.ConfirmJoin
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatComponents
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatEvents
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatNetwork.sendClientPacket
import java.util.*
import kotlin.random.Random.Default.nextFloat

object AttemptJoinOnDefeatHandler {
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

        CatchOnDefeatEvents.JOIN_DEFEAT_PRE.postThen(JoinDefeatEvent.Pre(
            player, pokemon
        ), {
            return
        }, {})

        val clonedPokemon = pokemon.clone()

        if (config.alwaysAcceptJoin) {
            finishJoin(player, clonedPokemon)
        } else {
            val holdingId = UUID.randomUUID()
            PokemonJoinPartyHolding.hold(PokemonJoinPartyHolding.Entry(holdingId, clonedPokemon, player))
            sendClientPacket(ConfirmJoin.Packet(holdingId, clonedPokemon.getDisplayName()), player)
        }
    }

    fun finishJoin(player: ServerPlayer, pokemon: Pokemon) {
        val storage = Cobblemon.storage.getParty(player)
        if (config.heal) pokemon.heal()
        storage.add(pokemon)
        if (config.countsAsCapture) {
            CobblemonEvents.POKEMON_CAPTURED.emit(
                PokemonCapturedEvent(
                    pokemon, player, EmptyPokeBallEntity(
                        PokeBalls.POKE_BALL, player.level()
                    )
                )
            )
        }
        player.sendSystemMessage(
            CatchOnDefeatComponents.joinedTeam(pokemon)
        )

        CatchOnDefeatEvents.JOIN_DEFEAT_POST.emit(
            JoinDefeatEvent.Post(player, pokemon)
        )
    }
}