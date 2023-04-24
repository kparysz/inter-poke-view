package com.inter.poke.view

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult.Page
import com.inter.poke.view.data.PokemonPagerDataSource
import com.inter.poke.view.network.PokemonRetrofitApi
import com.inter.poke.view.network.model.PokemonNetwork
import com.inter.poke.view.network.model.PokemonResponseNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class PokemonPagerDataSourceTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val pokemonRetrofitApi: PokemonRetrofitApi = mock()
    private val pokemonPagerDataSource = PokemonPagerDataSource(pokemonRetrofitApi)

    private val loadParams = LoadParams.Refresh(Int.MIN_VALUE, 20, false)

    private val pokemonNetworks = listOf(
        PokemonNetwork("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
        PokemonNetwork("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
        PokemonNetwork("venusaur", "https://pokeapi.co/api/v2/pokemon/3/")
    )

    @Test
    fun `load returns Page when successful`() = runBlocking {
        whenever(pokemonRetrofitApi.getPokemons(any())).doReturn(
            PokemonResponseNetwork(
                next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20", previous = null, results = listOf(
                    PokemonNetwork("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
                    PokemonNetwork("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
                    PokemonNetwork("venusaur", "https://pokeapi.co/api/v2/pokemon/3/")
                )
            )
        )

        val loadResult = pokemonPagerDataSource.load(loadParams)

        assertEquals(Page(pokemonNetworks, null, 20), loadResult)
    }

    @Test
    fun `load returns Error when exception is thrown`() = runBlocking {
        whenever(pokemonRetrofitApi.getPokemons(any())).thenAnswer { throw Exception() }

        val loadResult = pokemonPagerDataSource.load(loadParams)

        assert(loadResult is PagingSource.LoadResult.Error)
    }
}