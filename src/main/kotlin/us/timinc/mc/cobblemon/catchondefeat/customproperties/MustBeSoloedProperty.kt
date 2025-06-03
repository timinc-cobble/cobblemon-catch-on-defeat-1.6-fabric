package us.timinc.mc.cobblemon.catchondefeat.customproperties

import com.cobblemon.mod.common.api.properties.CustomPokemonPropertyType
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.properties.BooleanProperty
import us.timinc.mc.cobblemon.catchondefeat.customproperties.CatchOnDefeatProperties.CATCH_ON_DEFEAT

class MustBeSoloedProperty : CustomPokemonPropertyType<BooleanProperty> {
    override val keys: Iterable<String> = setOf("must_be_soloed")
    override val needsKey: Boolean = true

    override fun examples(): Collection<String> = setOf("yes", "no")

    override fun fromString(value: String?) = BooleanProperty(
        keys.first(),
        value == "yes",
        ::pokemonApplicator,
        ::entityApplicator,
        ::pokemonMatcher,
        ::entityMatcher
    )

    fun pokemonApplicator(pokemon: Pokemon, value: Boolean) {
        pokemon.persistentData.putBoolean(keys.first(), value)
        CATCH_ON_DEFEAT.pokemonApplicator(pokemon, true)
    }

    fun entityApplicator(entity: PokemonEntity, value: Boolean) {
        pokemonApplicator(entity.pokemon, value)
    }

    fun pokemonMatcher(pokemon: Pokemon, value: Boolean): Boolean =
        (pokemon.persistentData.contains(keys.first()) && pokemon.persistentData.getBoolean(keys.first())) == value

    fun entityMatcher(entity: PokemonEntity, value: Boolean): Boolean = pokemonMatcher(entity.pokemon, value)

}