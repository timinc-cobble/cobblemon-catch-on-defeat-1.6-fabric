package us.timinc.mc.cobblemon.catchondefeat.registry

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.network.chat.Component

object CatchOnDefeatComponents {
    fun cantCatch(pokemon: Pokemon) =
        Component.translatable("catch_on_defeat.feedback.cant_catch", pokemon.getDisplayName())

    fun thereCanOnlyBeOne(pokemon: Pokemon) = Component.translatable(
        "catch_on_defeat.feedback.there_can_only_be_one", pokemon.getDisplayName()
    )

    fun ranAway(pokemon: Pokemon) = Component.translatable(
        "catch_on_defeat.feedback.ran_away", pokemon.getDisplayName()
    )

    fun joinedTeam(pokemon: Pokemon) = Component.translatable(
        "catch_on_defeat.feedback.joined_team", pokemon.getDisplayName()
    )
}