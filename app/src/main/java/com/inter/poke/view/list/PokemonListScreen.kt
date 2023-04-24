package com.inter.poke.view.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.inter.poke.view.ui.compose.ErrorView
import com.inter.poke.view.ui.compose.LoadingView
import com.inter.poke.view.ui.theme.AppTheme
import com.inter.poke.view.ui.theme.PokeTypography

@Composable
fun PokemonListScreen(
    pokemonListScreenViewModel: PokemonListScreenViewModel = hiltViewModel(),
    onNavigateToPokemonDetail: (String) -> Unit
) {
    val state = pokemonListScreenViewModel.pokemons.collectAsLazyPagingItems()
    when (val data = state.loadState.refresh) {
        is LoadState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize().background(color = AppTheme.colors.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ErrorView(message = data.error.message ?: "")
            }
        }

        LoadState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize().background(color = AppTheme.colors.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingView(modifier = Modifier.size(96.dp))
            }
        }

        is LoadState.NotLoading -> {
            PokemonListContent(pokemons = state, onNavigateToPokemonDetail = onNavigateToPokemonDetail)
        }
    }
}

@Composable
fun PokemonListContent(
    pokemons: LazyPagingItems<PokemonViewData>,
    onNavigateToPokemonDetail: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(color = AppTheme.colors.background).padding(16.dp)
    ) {
        items(pokemons) { item ->
            item?.let {
                PokemonView(pokemonViewData = item, onNavigateToPokemonDetail = onNavigateToPokemonDetail)
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun PokemonView(
    pokemonViewData: PokemonViewData,
    onNavigateToPokemonDetail: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onNavigateToPokemonDetail(pokemonViewData.name)
            }
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                1.dp,
                color = AppTheme.colors.onBackground,
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Card(
            modifier = Modifier
                .size(48.dp)
                .border(
                    1.dp,
                    color = AppTheme.colors.onBackground,
                    RoundedCornerShape(50.dp)
                ),
            shape = CircleShape
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemonViewData.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = pokemonViewData.name,
            style = PokeTypography.h1,
            color = AppTheme.colors.onBackground,
            textAlign = TextAlign.Start
        )
    }
}

