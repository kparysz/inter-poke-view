package com.inter.poke.view.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inter.poke.view.data.GetPokemonDetailUseCase
import com.inter.poke.view.data.PokemonDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPokemonDetail: GetPokemonDetailUseCase
) : ViewModel() {

    private val uiStateMutableStateFlow = MutableStateFlow<PokemonDetailsUiState>(PokemonDetailsUiState.Loading)
    val uiState: StateFlow<PokemonDetailsUiState> = uiStateMutableStateFlow.asStateFlow()

    fun loadDetails() {
        val name: String = savedStateHandle["pokemonName"] ?: return

        viewModelScope.launch {
            runCatching {
                getPokemonDetail(name.lowercase())
            }.onSuccess {
                uiStateMutableStateFlow.value = it
            }.onFailure {
                uiStateMutableStateFlow.value = PokemonDetailsUiState.Error("General error message")
                it.printStackTrace()
            }
        }
    }
}