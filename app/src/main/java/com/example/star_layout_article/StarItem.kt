package com.example.star_layout_article

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarItem(
    selected: Boolean,
    radius: Dp = 32.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(if (selected) radius.times(1.2f) else radius)
            .background(if (selected) Color.Green else Color.Blue)
            .then(
                Modifier.border(if (selected) 4.dp else 0.dp, Color.Yellow, CircleShape)
            )
    ) {

    }
}




