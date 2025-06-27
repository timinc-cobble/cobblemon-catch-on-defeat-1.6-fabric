package us.timinc.mc.cobblemon.catchondefeat

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.scheduling.afterOnServer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.resources.ResourceLocation
import us.timinc.mc.cobblemon.catchondefeat.config.CatchOnDefeatConfig
import us.timinc.mc.cobblemon.catchondefeat.config.ConfigBuilder
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties
import us.timinc.mc.cobblemon.catchondefeat.event.handler.AttemptJoinOnDefeatHandler
import us.timinc.mc.cobblemon.catchondefeat.event.handler.CancelPokeballHitWhenOnlyJoinByDefeatHandler
import us.timinc.mc.cobblemon.catchondefeat.registry.CatchOnDefeatNetwork

object CatchOnDefeatMod : ModInitializer {
    @Suppress("MemberVisibilityCanBePrivate")
    const val MOD_ID = "catchondefeat"

    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var config: CatchOnDefeatConfig

    override fun onInitialize() {
        config = ConfigBuilder.load(CatchOnDefeatConfig::class.java, MOD_ID)

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register { _, _, _ ->
            config = ConfigBuilder.load(CatchOnDefeatConfig::class.java, MOD_ID)
        }

        var initialized = false
        ServerLifecycleEvents.SERVER_STARTED.register { evt ->
            if (initialized) return@register
            initialized = true
            afterOnServer(1, evt.overworld()) {
                CatchOnDefeatProperties.register()
            }
        }

        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.LOWEST, AttemptJoinOnDefeatHandler::handle)
        CobblemonEvents.THROWN_POKEBALL_HIT.subscribe(
            Priority.LOWEST,
            CancelPokeballHitWhenOnlyJoinByDefeatHandler::handle
        )
        CatchOnDefeatNetwork
    }

    fun modResource(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
}