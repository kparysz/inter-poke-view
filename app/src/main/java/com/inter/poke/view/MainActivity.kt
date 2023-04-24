package com.inter.poke.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inter.poke.view.ui.compose.PokeAppView
import com.inter.poke.view.ui.theme.InterPokeViewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterPokeViewTheme {
                PokeAppView()
            }
        }
    }
}