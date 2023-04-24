package com.inter.poke.view.ui.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    message: String
) {
    Text(text = message, color = Color.White)
}