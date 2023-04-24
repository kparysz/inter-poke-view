package com.inter.poke.view.data

import androidx.compose.runtime.Stable
import com.haroldadmin.cnradapter.NetworkResponse
import com.inter.poke.view.details.PokemonDetailViewData
import com.inter.poke.view.details.PokemonStats
import com.inter.poke.view.network.PokemonRetrofitApi
import java.util.Locale
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val pokemonApi: PokemonRetrofitApi
) {

    suspend operator fun invoke(name: String): PokemonDetailsUiState {
        val response = pokemonApi.getPokemonDetails(name)

        if (response !is NetworkResponse.Success) return PokemonDetailsUiState.Error("Failed to get Pokemon details")
        val pokemonResponse = response.body
        val imageUrl = pokemonResponse.sprites.frontDefault

        val types = pokemonResponse.types.map { it.type.name }

        val moveset = pokemonResponse.moves.map { it.move.name }

        val speciesResponse = pokemonApi.getPokemonSpecies(pokemonResponse.id)

        if (speciesResponse !is NetworkResponse.Success) return PokemonDetailsUiState.Error("pokemonApi.getPokemonSpecies(pokemonResponse.id)")
        val evolutionLine = speciesResponse.body.evolutionChain.url

        val pokedexEntry = speciesResponse.body.flavorTextEntries.find { it.language.name == "en" }?.flavorText ?: ""

        val baseStats = PokemonStats(
            hp = pokemonResponse.stats.find { it.stat.name == "hp" }?.baseStat ?: 0,
            attack = pokemonResponse.stats.find { it.stat.name == "attack" }?.baseStat ?: 0,
            defense = pokemonResponse.stats.find { it.stat.name == "defense" }?.baseStat ?: 0,
            specialAttack = pokemonResponse.stats.find { it.stat.name == "special-attack" }?.baseStat ?: 0,
            specialDefense = pokemonResponse.stats.find { it.stat.name == "special-defense" }?.baseStat ?: 0,
            speed = pokemonResponse.stats.find { it.stat.name == "speed" }?.baseStat ?: 0
        )

        val effortValues = PokemonStats(
            hp = pokemonResponse.stats.find { it.stat.name == "hp" }?.effort ?: 0,
            attack = pokemonResponse.stats.find { it.stat.name == "attack" }?.effort ?: 0,
            defense = pokemonResponse.stats.find { it.stat.name == "defense" }?.effort ?: 0,
            specialAttack = pokemonResponse.stats.find { it.stat.name == "special-attack" }?.effort ?: 0,
            specialDefense = pokemonResponse.stats.find { it.stat.name == "special-defense" }?.effort ?: 0,
            speed = pokemonResponse.stats.find { it.stat.name == "speed" }?.effort ?: 0
        )

        val description = speciesResponse.body.flavorTextEntries.find { it.language.name == "en" }?.flavorText?.replace("\n", " ") ?: ""

        return PokemonDetailsUiState.Loaded(
            PokemonDetailViewData(
                id = pokemonResponse.id,
                name = pokemonResponse.name.capitalize(Locale.ROOT),
                imageUrl = imageUrl ?: "",
                types = types,
                moveset = moveset,
                evolutionLine = listOf(),
                pokedexEntry = pokedexEntry,
                baseStats = baseStats,
                effortValues = effortValues,
                description = description
            )
        )
    }
}

sealed interface PokemonDetailsUiState {
    @Stable
    data class Loaded(val data: PokemonDetailViewData) : PokemonDetailsUiState

    @Stable
    data class Error(val message: String) : PokemonDetailsUiState

    object Loading : PokemonDetailsUiState
}