package com.inter.poke.view.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.inter.poke.view.details.DetailScreen
import com.inter.poke.view.list.PokemonListScreen
import com.inter.poke.view.navigation.Arguments
import com.inter.poke.view.navigation.Screen

@OptIn(
    ExperimentalMaterialNavigationApi::class,
    ExperimentalAnimationApi::class,
)
@Composable
fun PokeAppView() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navHostController = rememberAnimatedNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(12.dp),
        sheetBackgroundColor = Color.Transparent,
        content = {
            Scaffold(
                topBar = {
                }
            ) { paddingValues ->
                AnimatedNavHost(navController = navHostController, startDestination = Screen.Home.route, Modifier.padding(paddingValues)) {
                    getNavigationGraph(navHostController)
                }
            }
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.getNavigationGraph(navHostController: NavHostController) {
    composable(route = Screen.Home.route) {
        PokemonListScreen() { pokemonId ->
            navHostController.navigate(Screen.Detail.createRoute(pokemonId))
        }
    }
    composable(
        route = Screen.Detail.route,
        arguments = listOf(navArgument(Arguments.POKEMON_NAME_DETAILS_SCREEN_ARGUMENT_KEY) { type = NavType.StringType })
    ) {
        DetailScreen()
    }
}