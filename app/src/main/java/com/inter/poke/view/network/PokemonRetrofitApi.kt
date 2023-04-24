package com.inter.poke.view.network

import com.haroldadmin.cnradapter.NetworkResponse
import com.inter.poke.view.network.model.PokeApiError
import com.inter.poke.view.network.model.PokemonResponse
import com.inter.poke.view.network.model.PokemonResponseNetwork
import com.inter.poke.view.network.model.PokemonSpecies
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonRetrofitApi {

    @GET("api/v2/pokemon?limit=100")
    @Headers("Content-Type: application/json")
    suspend fun getPokemons(@Query("offset") offset: Int): PokemonResponseNetwork

    @GET("api/v2/pokemon/{name}")
    @Headers("Content-Type: application/json")
    suspend fun getPokemonDetails(@Path("name") name: String): NetworkResponse<PokemonResponse, PokeApiError>

    @GET("api/v2/pokemon-species/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getPokemonSpecies(@Path("id") id: Int): NetworkResponse<PokemonSpecies, PokeApiError>
}