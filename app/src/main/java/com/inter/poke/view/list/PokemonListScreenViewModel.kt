package com.inter.poke.view.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.inter.poke.view.data.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonListScreenViewModel @Inject constructor(
    getPokemonList: GetPokemonListUseCase,
) : ViewModel() {

    val pokemons = getPokemonList.invoke().cachedIn(viewModelScope)

}