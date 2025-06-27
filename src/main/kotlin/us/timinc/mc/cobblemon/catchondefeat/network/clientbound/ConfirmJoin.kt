package us.timinc.mc.cobblemon.catchondefeat.network.clientbound

import io.wispforest.owo.network.ClientAccess
import net.minecraft.network.chat.Component
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatClientMod.config
import us.timinc.mc.cobblemon.catchondefeat.api.network.ClientNetworkHandler
import us.timinc.mc.cobblemon.catchondefeat.network.serverbound.JoinConfirmed
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatNetwork.sendServerPacket
import us.timinc.mc.cobblemon.catchondefeat.screen.ConfirmJoinScreen
import java.util.UUID

object ConfirmJoin : ClientNetworkHandler<ConfirmJoin.Packet>() {
    @JvmRecord
    data class Packet(val id: UUID, val name: Component) {
        private fun respond(accepted: Boolean) {
            sendServerPacket(JoinConfirmed.Packet(id, accepted))
        }

        fun accept() = respond(true)
        fun reject() = respond(false)
    }

    override fun handle(data: Packet, clientAccess: ClientAccess) {
        if (config.alwaysAccept) {
            data.accept()
            return
        }
        clientAccess.runtime().setScreen(ConfirmJoinScreen(data))
    }

    override val packetClass = Packet::class.java
}

