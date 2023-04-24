package com.inter.poke.view.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<Type>,
    val moves: List<Move>,
    val stats: List<Stat>
)

@Serializable
data class Sprites(
    @SerialName("front_default")
    val frontDefault: String?,
    @SerialName("front_shiny")
    val frontShiny: String?,
    @SerialName("front_female")
    val frontFemale: String?,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String?,
    @SerialName("back_default")
    val backDefault: String?,
    @SerialName("back_shiny")
    val backShiny: String?,
    @SerialName("back_female")
    val backFemale: String?,
    @SerialName("back_shiny_female")
    val backShinyFemale: String?
)

@Serializable
data class Type(
    val slot: Int,
    val type: TypeDetails
)

@Serializable
data class TypeDetails(
    val name: String,
    val url: String
)

@Serializable
data class Move(
    val move: MoveDetails
)

@Serializable
data class MoveDetails(
    val name: String,
    val url: String
)

@Serializable
data class Stat(
    @SerialName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatDetails
)

@Serializable
data class StatDetails(
    val name: String,
    val url: String
)