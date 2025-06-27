package us.timinc.mc.cobblemon.catchondefeat.registry

import io.wispforest.owo.network.OwoNetChannel
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatMod.modResource
import us.timinc.mc.cobblemon.catchondefeat.network.clientbound.ConfirmJoin
import us.timinc.mc.cobblemon.catchondefeat.network.serverbound.JoinConfirmed

object CatchOnDefeatNetwork {
    val COD_CHANNEL: OwoNetChannel = OwoNetChannel.create(modResource("main"))

    init {
        COD_CHANNEL.registerClientbound(ConfirmJoin.packetClass, ConfirmJoin::handle)
        COD_CHANNEL.registerServerbound(JoinConfirmed.packetClass, JoinConfirmed::handle)
    }

    fun <T : Record> sendServerPacket(packet: T) {
        COD_CHANNEL.clientHandle().send(packet)
    }

    fun <T : Record> sendClientPacket(packet: T, player: ServerPlayer) {
        COD_CHANNEL.serverHandle(player).send(packet)
    }
}