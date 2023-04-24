package com.inter.poke.view.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeApiError(
    val detail: String,
    val status: Int,
    val title: String,
    @SerialName("type")
    val errorType: String
)