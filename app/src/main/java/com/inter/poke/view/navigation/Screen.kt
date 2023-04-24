package com.inter.poke.view.navigation

import com.inter.poke.view.navigation.Arguments.POKEMON_NAME_DETAILS_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{$POKEMON_NAME_DETAILS_SCREEN_ARGUMENT_KEY}") {
        fun createRoute(pokemonName: String) = "detail/$pokemonName"
    }
}

object Arguments {
    const val POKEMON_NAME_DETAILS_SCREEN_ARGUMENT_KEY = "pokemonName"
}