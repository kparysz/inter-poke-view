package com.inter.poke.view.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonSpecies(
    @SerialName("evolution_chain")
    val evolutionChain: EvolutionChain,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavorText>,
)

@Serializable
data class EvolutionChain(
    val url: String
)

@Serializable
data class FlavorText(
    @SerialName("flavor_text")
    val flavorText: String,
    val language: Language,
)

@Serializable
data class Language(
    val name: String,
    val url: String
)