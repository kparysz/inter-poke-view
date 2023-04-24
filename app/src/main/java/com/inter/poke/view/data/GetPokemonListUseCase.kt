package com.inter.poke.view.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.inter.poke.view.list.PokemonViewData
import com.inter.poke.view.network.PokemonRetrofitApi
import com.inter.poke.view.network.model.PokemonNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonApi: PokemonRetrofitApi
) {

    private val pager = Pager(PagingConfig(pageSize = 20)) {
        PokemonPagerDataSource(pokemonApi)
    }

    operator fun invoke(): Flow<PagingData<PokemonViewData>> = pager.flow.map { pagingData ->
        pagingData.map {
            it.mapToDomain()
        }
    }

    private fun PokemonNetwork.mapToDomain() = PokemonViewData(
        name = this.name.capitalize(Locale.getDefault()),
        imageUrl = getPokemonAvatar(this.url.split("/").takeLast(2).firstOrNull() ?: "")
    )

    private fun getPokemonAvatar(id: String) = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}