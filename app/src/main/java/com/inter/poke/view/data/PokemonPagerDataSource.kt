package com.inter.poke.view.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.inter.poke.view.network.PokemonRetrofitApi
import com.inter.poke.view.network.model.PokemonNetwork
import javax.inject.Inject

class PokemonPagerDataSource @Inject constructor(
    private val pokemonRetrofitApi: PokemonRetrofitApi
) : PagingSource<Int, PokemonNetwork>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonNetwork>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonNetwork> = try {
        val nextPageNumber = params.key ?: 0
        val response = pokemonRetrofitApi.getPokemons(nextPageNumber)

        val pattern = "offset=(\\d+)".toRegex()
        val matchNext = pattern.find(response.next)
        val offsetNext = matchNext?.groupValues?.get(1)?.toIntOrNull() ?: 0
        LoadResult.Page(
            data = response.results,
            prevKey = null,
            nextKey = offsetNext
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}