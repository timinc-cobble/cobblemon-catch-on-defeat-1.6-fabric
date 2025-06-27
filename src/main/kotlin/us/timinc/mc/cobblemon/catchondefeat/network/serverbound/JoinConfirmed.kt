package us.timinc.mc.cobblemon.catchondefeat.network.serverbound

import io.wispforest.owo.network.ServerAccess
import us.timinc.mc.cobblemon.catchondefeat.api.network.ServerNetworkHandler
import us.timinc.mc.cobblemon.catchondefeat.event.handler.AttemptJoinOnDefeatHandler
import us.timinc.mc.cobblemon.catchondefeat.holding.PokemonJoinPartyHolding
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatComponents.wasReleased
import java.util.UUID

object JoinConfirmed : ServerNetworkHandler<JoinConfirmed.Packet>() {
    @JvmRecord
    data class Packet(val id: UUID, val accepted: Boolean)

    override val packetClass = Packet::class.java

    override fun handle(packet: Packet, serverAccess: ServerAccess) {
        val holding = PokemonJoinPartyHolding.get(packet.id) ?: return
        if (!packet.accepted) {
            holding.player.sendSystemMessage(wasReleased(holding.pokemon.getDisplayName()))
            return
        }
        AttemptJoinOnDefeatHandler.finishJoin(holding.player, holding.pokemon)
    }
}