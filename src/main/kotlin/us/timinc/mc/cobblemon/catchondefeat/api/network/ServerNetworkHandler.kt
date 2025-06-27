package us.timinc.mc.cobblemon.catchondefeat.api.network

import io.wispforest.owo.network.ServerAccess

abstract class ServerNetworkHandler<T : Any> {
    abstract val packetClass: Class<T>
    abstract fun handle(packet: T, serverAccess: ServerAccess)
}