package com.inter.poke.view

import com.haroldadmin.cnradapter.NetworkResponse
import com.inter.poke.view.data.GetPokemonDetailUseCase
import com.inter.poke.view.data.PokemonDetailsUiState
import com.inter.poke.view.details.PokemonDetailViewData
import com.inter.poke.view.details.PokemonStats
import com.inter.poke.view.network.PokemonRetrofitApi
import com.inter.poke.view.network.model.EvolutionChain
import com.inter.poke.view.network.model.Move
import com.inter.poke.view.network.model.MoveDetails
import com.inter.poke.view.network.model.PokeApiError
import com.inter.poke.view.network.model.PokemonResponse
import com.inter.poke.view.network.model.PokemonSpecies
import com.inter.poke.view.network.model.Sprites
import com.inter.poke.view.network.model.Type
import com.inter.poke.view.network.model.TypeDetails
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class GetPokemonDetailUseCaseTest {

    @Mock
    private lateinit var pokemonApi: PokemonRetrofitApi

    private lateinit var useCase: GetPokemonDetailUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetPokemonDetailUseCase(pokemonApi)
    }

    @Test
    fun `invoke should return Loaded state with Pokemon details when network response is success`() {
        val body = PokemonResponse(
            id = 25, name = "Pikachu", sprites = Sprites(
                frontDefault = null,
                frontShiny = null,
                frontFemale = null,
                frontShinyFemale = null,
                backDefault = null,
                backShiny = null,
                backFemale = null,
                backShinyFemale = null,
            ), types = listOf(Type(1, TypeDetails("electric", ""))), moves = listOf(
                Move(
                    move = MoveDetails(
                        name = "thunderbolt",
                        url = "",
                    )
                )
            ), stats = listOf()
        )
        runBlocking {
            val name = "pikachu"
            val spriteUrl = "https://pokeapi.co/media/sprites/pokemon/25.png"
            val types = listOf("electric")
            val moves = listOf("thunderbolt")
            val flavorText = "When several of these Pokémon gather, their electricity could build and cause lightning storms."
            val baseStats = PokemonStats(hp = 35, attack = 55, defense = 40, specialAttack = 50, specialDefense = 50, speed = 90)
            val effortValues = PokemonStats(hp = 0, attack = 0, defense = 0, specialAttack = 0, specialDefense = 0, speed = 2)
            val pokemon = PokemonDetailViewData(25, "Pikachu", spriteUrl, types, moves, emptyList(), flavorText, baseStats, effortValues, flavorText)
            val success = NetworkResponse.Success<PokemonResponse, PokeApiError>(body, Response.success(null))
            val successPokemonSpecies = NetworkResponse.Success<PokemonSpecies, PokeApiError>(
                PokemonSpecies(evolutionChain = EvolutionChain(url = ""), flavorTextEntries = listOf()),
                Response.success(null)
            )
            `when`(pokemonApi.getPokemonDetails(name)).thenReturn(success)
            `when`(pokemonApi.getPokemonSpecies(25)).thenReturn(successPokemonSpecies)

            val result = useCase(name)

            Assertions.assertThat(result).isInstanceOf(PokemonDetailsUiState.Loaded::class.java)
            result as PokemonDetailsUiState.Loaded
            Assertions.assertThat(result.data.name).isEqualTo(pokemon.name)
        }
    }

    @Test
    fun `invoke should return Error state with error message when network response is not success`() = runBlocking {
        val name = "pikachu"
        val errorMessage = "Failed to get Pokemon details"
        val errorResponse = NetworkResponse.ServerError<PokemonResponse, PokeApiError>(
            body = PokeApiError(detail = "definiebas", status = 8331, title = "parturient", errorType = "vidisse"),
            response = null
        )
        `when`(pokemonApi.getPokemonDetails(name)).thenReturn(errorResponse)

        val result = useCase(name)

        assertEquals(PokemonDetailsUiState.Error(errorMessage), result)
    }
}