package us.timinc.mc.cobblemon.catchondefeat.holding

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerPlayer
import java.util.*

object PokemonJoinPartyHolding {
    private val holding: MutableMap<UUID, Entry> = mutableMapOf()

    data class Entry(val id: UUID, val pokemon: Pokemon, val player: ServerPlayer)

    fun hold(toHold: Entry) {
        holding[toHold.id] = toHold
    }

    fun get(id: UUID): Entry? {
        val value = holding[id]
        if (value != null) holding.remove(id)
        return value
    }
}