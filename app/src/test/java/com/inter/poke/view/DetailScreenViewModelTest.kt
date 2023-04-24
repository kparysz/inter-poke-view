package com.inter.poke.view

import androidx.lifecycle.SavedStateHandle
import com.inter.poke.view.data.GetPokemonDetailUseCase
import com.inter.poke.view.data.PokemonDetailsUiState
import com.inter.poke.view.details.DetailScreenViewModel
import com.inter.poke.view.details.PokemonDetailViewData
import com.inter.poke.view.details.PokemonStats
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi

class DetailScreenViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()


    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getPokemonDetail: GetPokemonDetailUseCase
    private lateinit var viewModel: DetailScreenViewModel

    @Before
    fun setUp() {
        savedStateHandle = mock(SavedStateHandle::class.java)
        getPokemonDetail = mock(GetPokemonDetailUseCase::class.java)
        viewModel = DetailScreenViewModel(savedStateHandle, getPokemonDetail)
        Dispatchers.setMain(coroutineTestRule.testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDetails should update uiState when getPokemonDetail returns success`() = runBlocking {
        val pokemonName = "Pikachu"
        val pokemonDetailsUiState = PokemonDetailsUiState.Loaded(
            data
        )
        `when`(savedStateHandle.get<String>("pokemonName")).thenReturn(pokemonName)
        `when`(getPokemonDetail(pokemonName.lowercase())).thenReturn(pokemonDetailsUiState)

        viewModel.loadDetails()

        verify(getPokemonDetail).invoke(pokemonName.lowercase())
        assertEquals(pokemonDetailsUiState, viewModel.uiState.first())
    }

    @Test
    fun `loadDetails should update uiState when getPokemonDetail returns failure`() = runBlocking {
        val pokemonName = "Pikachu"
        val errorMessage = "Failed to load Pokemon details"
        val exception = Exception(errorMessage)
        val pokemonDetailsUiState = PokemonDetailsUiState.Error("General error message")
        `when`(savedStateHandle.get<String>("pokemonName")).thenReturn(pokemonName)
        `when`(getPokemonDetail(pokemonName.lowercase())).thenAnswer { throw exception }

        viewModel.loadDetails()

        verify(getPokemonDetail).invoke(pokemonName.lowercase())
        assertEquals(pokemonDetailsUiState, viewModel.uiState.first())
    }

    private val data = PokemonDetailViewData(
        id = 2180,
        name = "Vincent Velez",
        imageUrl = "https://search.test.com/search?p=non",
        types = listOf(),
        moveset = listOf(),
        evolutionLine = listOf(),
        pokedexEntry = "fabulas",
        baseStats = PokemonStats(hp = 8378, attack = 8305, defense = 3506, specialAttack = 3682, specialDefense = 1362, speed = 4748),
        effortValues = PokemonStats(hp = 1309, attack = 9013, defense = 2554, specialAttack = 7383, specialDefense = 8937, speed = 2030),
        description = "accumsan"
    )
}