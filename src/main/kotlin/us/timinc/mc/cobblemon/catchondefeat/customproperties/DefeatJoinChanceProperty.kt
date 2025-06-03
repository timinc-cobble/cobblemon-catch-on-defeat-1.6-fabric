package us.timinc.mc.cobblemon.catchondefeat.customproperties

import com.cobblemon.mod.common.api.properties.CustomPokemonPropertyType
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.properties.FloatProperty
import org.apache.commons.lang3.math.NumberUtils.toFloat
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.CATCH_ON_DEFEAT

class DefeatJoinChanceProperty : CustomPokemonPropertyType<FloatProperty> {
    override val keys: Iterable<String> = setOf("defeat_join_chance")
    override val needsKey: Boolean = true

    override fun examples(): Collection<String> = setOf("yes", "no")

    override fun fromString(value: String?) = FloatProperty(
        keys.first(),
        toFloat(value),
        ::pokemonApplicator,
        ::entityApplicator,
        ::pokemonMatcher,
        ::entityMatcher
    )

    fun pokemonApplicator(pokemon: Pokemon, value: Float) {
        pokemon.persistentData.putFloat(keys.first(), value)
        CATCH_ON_DEFEAT.pokemonApplicator(pokemon, true)
    }

    fun entityApplicator(entity: PokemonEntity, value: Float) {
        pokemonApplicator(entity.pokemon, value)
    }

    fun getValue(pokemon: Pokemon): Float? =
        if (!pokemon.persistentData.contains(keys.first())) null
        else pokemon.persistentData.getFloat(keys.first())

    fun pokemonMatcher(pokemon: Pokemon, value: Float): Boolean =
        pokemon.persistentData.contains(keys.first()) && (pokemon.persistentData.getFloat(keys.first()) == value)

    fun entityMatcher(entity: PokemonEntity, value: Float): Boolean = pokemonMatcher(entity.pokemon, value)
}