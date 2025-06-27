package us.timinc.mc.cobblemon.catchondefeat.api.network

import io.wispforest.owo.network.ClientAccess

abstract class ClientNetworkHandler<T : Any> {
    abstract fun handle(data: T, clientAccess: ClientAccess)
    abstract val packetClass: Class<T>
}