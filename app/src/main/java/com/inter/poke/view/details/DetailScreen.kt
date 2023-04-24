package com.inter.poke.view.details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.inter.poke.view.R
import com.inter.poke.view.data.PokemonDetailsUiState
import com.inter.poke.view.ui.compose.ErrorView
import com.inter.poke.view.ui.compose.LoadingView
import com.inter.poke.view.ui.theme.AppTheme
import java.util.Locale

@Composable
fun DetailScreen(viewModel: DetailScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDetails()
    }

    when (val data = uiState) {
        is PokemonDetailsUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize().background(color = AppTheme.colors.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ErrorView(message = data.message)
            }
        }

        is PokemonDetailsUiState.Loaded -> {
            PokemonDetails(data.data)
        }

        PokemonDetailsUiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize().background(color = AppTheme.colors.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingView(modifier = Modifier.size(96.dp))
            }
        }
    }
}

@Composable
fun PokemonDetails(pokemon: PokemonDetailViewData) {
    val expansionState = remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expansionState.value = !expansionState.value },
        elevation = 8.dp,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(16.dp)
        ) {
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = pokemon.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemon.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(CenterVertically)
                    )
                }

                if (expansionState.value) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.pokemon_details_screen_stats_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column {
                        Text(
                            text = stringResource(R.string.pokemon_details_screen_stats_hp, pokemon.effortValues.hp),
                            fontSize = 18.sp
                        )
                        Text(
                            text = stringResource(R.string.pokemon_details_screen_stats_attack, pokemon.effortValues.attack),
                            fontSize = 18.sp
                        )
                        Text(
                            text = stringResource(R.string.pokemon_details_screen_stats_defense, pokemon.effortValues.defense),
                            fontSize = 18.sp
                        )
                        Text(
                            text = stringResource(R.string.pokemon_details_screen_stats_special_attack, pokemon.effortValues.specialAttack),
                            fontSize = 18.sp
                        )
                        Text(
                            text = stringResource(R.string.pokemon_details_screen_stats_special_defense, pokemon.effortValues.specialDefense),
                            fontSize = 18.sp
                        )
                        Text(
                            text = stringResource(R.string.pokemon_details_screen_stats_speed, pokemon.effortValues.speed),
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.pokemon_details_screen_additional_info_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PokemonTypesView(pokemon.types)

                    PokemonEvolutionLineView(pokemon.evolutionLine)

                    PokemonMovesetView(pokemon.moveset)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = pokemon.pokedexEntry,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.pokemon_details_screen_base_stats_title),
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        StatRow(
                            statName = stringResource(R.string.pokemon_details_screen_base_stats_hp),
                            baseStatValue = pokemon.baseStats.hp,
                            maxStatValue = 255,
                            backgroundColor = Color.Red
                        )
                        StatRow(
                            statName = stringResource(R.string.pokemon_details_screen_base_stats_attack),
                            baseStatValue = pokemon.baseStats.attack,
                            maxStatValue = 255,
                            backgroundColor = Color(0xFFFFAA2B)
                        )
                        StatRow(
                            statName = stringResource(R.string.pokemon_details_screen_base_stats_defense),
                            baseStatValue = pokemon.baseStats.defense,
                            maxStatValue = 255,
                            backgroundColor = Color.Yellow
                        )
                        StatRow(
                            statName = stringResource(R.string.pokemon_details_screen_base_stats_special_attack),
                            baseStatValue = pokemon.baseStats.specialAttack,
                            maxStatValue = 255,
                            backgroundColor = Color.Green
                        )
                        StatRow(
                            statName = stringResource(R.string.pokemon_details_screen_base_stats_special_defense),
                            baseStatValue = pokemon.baseStats.specialDefense,
                            maxStatValue = 255,
                            backgroundColor = Color.Blue
                        )
                        StatRow(
                            statName = stringResource(R.string.pokemon_details_screen_base_stats_speed),
                            baseStatValue = pokemon.baseStats.speed,
                            maxStatValue = 255,
                            backgroundColor = Color(0xFF7768B3)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

}

@Composable
fun StatRow(
    statName: String,
    baseStatValue: Int,
    maxStatValue: Int,
    backgroundColor: Color
) {
    val progress = baseStatValue.toFloat() / maxStatValue.toFloat()
    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = statName,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(80.dp)
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .height(16.dp)
                .padding(horizontal = 8.dp),
            color = backgroundColor
        )
        Text(
            text = baseStatValue.toString(),
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(40.dp)
        )
    }
}

@Composable
fun PokemonTypesView(types: List<String>) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.pokemon_details_screen_types_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )

        Row(
            modifier = Modifier.wrapContentWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            types.forEach { type ->
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(colorForType(type), CircleShape)
                ) {
                    Text(
                        text = type,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonMovesetView(moveset: List<String>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.pokemon_details_screen_moveset_title),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        FlowRow(
            mainAxisSize = SizeMode.Expand,
            mainAxisAlignment = FlowMainAxisAlignment.Start
        ) {
            moveset.forEach { move ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = move,
                        style = MaterialTheme.typography.body1,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonEvolutionLineView(evolutionLine: List<String>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.pokemon_details_screen_evolution_line_title),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for ((index, pokemonName) in evolutionLine.withIndex()) {
                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://img.pokemondb.net/artwork/${pokemonName.lowercase(Locale.ROOT)}.jpg")
                                .crossfade(true)
                                .build(),
                            contentDescription = pokemonName,
                            modifier = Modifier.size(96.dp)
                        )
                        Text(
                            text = pokemonName,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    if (index < evolutionLine.size - 1) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Evolution arrow",
                            tint = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun colorForType(type: String): Color {
    return when (type.capitalize()) {
        "Normal" -> Color(200, 200, 160)
        "Fire" -> Color(240, 128, 48)
        "Water" -> Color(104, 144, 240)
        "Grass" -> Color(120, 200, 80)
        "Electric" -> Color(248, 208, 48)
        "Ice" -> Color(152, 216, 216)
        "Fighting" -> Color(192, 48, 40)
        "Poison" -> Color(160, 64, 160)
        "Ground" -> Color(224, 192, 104)
        "Flying" -> Color(168, 144, 240)
        "Psychic" -> Color(248, 88, 136)
        "Bug" -> Color(168, 184, 32)
        "Rock" -> Color(184, 160, 56)
        "Ghost" -> Color(112, 88, 152)
        "Dragon" -> Color(112, 56, 248)
        "Dark" -> Color(112, 88, 72)
        "Steel" -> Color(184, 184, 208)
        "Fairy" -> Color(238, 153, 172)
        else -> Color.Gray
    }
}

//val types = listOf("Fire", "Flying")
//val moveset = listOf(
//    "Flamethrower",
//    "Dragon Claw",
//    "Air Slash",
//    "Roost"
//)
//val pokemon = PokemonDetailViewData(
//    id = 6,
//    name = "Charizard",
//    imageUrl = "https://img.pokemondb.net/artwork/charizard.jpg",
//    types = types,
//    moveset = moveset,
//    evolutionLine = listOf("Charmander", "Charmeleon", "Charizard"),
//    pokedexEntry = "Charizard flies around the sky in search of powerful opponents. It breathes fire of such great heat that it melts anything. However, it never turns its fiery breath on any opponent weaker than itself.",
//    baseStats = PokemonStats(
//        hp = 78,
//        attack = 84,
//        defense = 78,
//        specialAttack = 109,
//        specialDefense = 85,
//        speed = 100
//    ),
//    effortValues = PokemonStats(
//        hp = 0,
//        attack = 0,
//        defense = 0,
//        specialAttack = 3,
//        specialDefense = 0,
//        speed = 0
//    ),
//    description = "Charizard is a dual-type Fire/Flying Pokémon introduced in Generation I. It evolves from Charmeleon starting at level 36. It is the final form of Charmander. Charizard can Mega Evolve into either Mega Charizard X using the Charizardite X or Mega Charizard Y using the Charizardite Y."
//)