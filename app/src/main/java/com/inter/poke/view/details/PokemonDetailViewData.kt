package com.inter.poke.view.details

import androidx.compose.runtime.Stable

@Stable
data class PokemonDetailViewData(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val moveset: List<String>,
    val evolutionLine: List<String>,
    val pokedexEntry: String,
    val baseStats: PokemonStats,
    val effortValues: PokemonStats,
    val description: String
)

@Stable
data class PokemonStats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
)