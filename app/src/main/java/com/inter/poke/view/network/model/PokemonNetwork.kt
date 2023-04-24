package com.inter.poke.view.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponseNetwork(
    val next: String,
    val previous: String?,
    val results: List<PokemonNetwork>
)

@Serializable
data class PokemonNetwork(
    val name: String,
    val url: String
)

@Serializable
data class PokemonDetailResponseNetwork(
    val next: String,
    val previous: String?,
    val results: List<PokemonNetwork>
)

