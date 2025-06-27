package us.timinc.mc.cobblemon.catchondefeat

import net.fabricmc.api.ClientModInitializer
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeatMod.MOD_ID
import us.timinc.mc.cobblemon.catchondefeat.config.CatchOnDefeatClientConfig
import us.timinc.mc.cobblemon.catchondefeat.config.ConfigBuilder

object CatchOnDefeatClientMod : ClientModInitializer {
    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var config: CatchOnDefeatClientConfig

    override fun onInitializeClient() {
        config = ConfigBuilder.load(CatchOnDefeatClientConfig::class.java, "${MOD_ID}_client")
    }
}