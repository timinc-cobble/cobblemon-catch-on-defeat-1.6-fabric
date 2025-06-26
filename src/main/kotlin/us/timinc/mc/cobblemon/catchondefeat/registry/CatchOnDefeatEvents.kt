package us.timinc.mc.cobblemon.catchondefeat.registry

import com.cobblemon.mod.common.api.reactive.CancelableObservable
import com.cobblemon.mod.common.api.reactive.EventObservable
import us.timinc.mc.cobblemon.catchondefeat.event.JoinDefeatEvent

object CatchOnDefeatEvents {
    @JvmField
    val JOIN_DEFEAT_PRE = CancelableObservable<JoinDefeatEvent.Pre>()

    @JvmField
    val JOIN_DEFEAT_POST = EventObservable<JoinDefeatEvent.Post>()
}