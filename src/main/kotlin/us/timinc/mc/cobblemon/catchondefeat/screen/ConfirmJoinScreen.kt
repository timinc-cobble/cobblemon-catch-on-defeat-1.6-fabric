package us.timinc.mc.cobblemon.catchondefeat.screen

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.ButtonComponent
import io.wispforest.owo.ui.component.LabelComponent
import io.wispforest.owo.ui.container.FlowLayout
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatMod.modResource
import us.timinc.mc.cobblemon.catchondefeat.network.clientbound.ConfirmJoin
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatComponents.wouldLikeToJoinTeam

class ConfirmJoinScreen(val packet: ConfirmJoin.Packet) :
    BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(modResource("confirm_join"))) {
    private var responded: Boolean = false

    override fun build(layout: FlowLayout) {
        val confirmLabel = layout.childById(LabelComponent::class.java, "confirm-label")
        confirmLabel.text(wouldLikeToJoinTeam(packet.name))

        val acceptButton = layout.childById(ButtonComponent::class.java, "accept-button")
        acceptButton.onPress {
            responded = true
            packet.accept()
            onClose()
        }

        val rejectButton = layout.childById(ButtonComponent::class.java, "reject-button")
        rejectButton.onPress {
            responded = true
            packet.reject()
            onClose()
        }
    }

    override fun dispose() {
        super.dispose()
        if (!responded) packet.reject()
    }
}
