package com.example.lab13_animaciones

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SizeAndPositionExample() {
    var moved by remember { mutableStateOf(false) }

    val boxSize by animateDpAsState(
        targetValue = if (moved) 120.dp else 60.dp,
        animationSpec = tween(durationMillis = 500),
        label = "BoxSizeAnimation"
    )

    val offsetX by animateDpAsState(
        targetValue = if (moved) 120.dp else 0.dp,
        animationSpec = tween(durationMillis = 500),
        label = "OffsetXAnimation"
    )

    val offsetY by animateDpAsState(
        targetValue = if (moved) (-80).dp else 0.dp,
        animationSpec = tween(durationMillis = 500),
        label = "OffsetYAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = offsetX, y = offsetY)
                .size(boxSize)
                .background(Color(0xFFFFA726))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { moved = !moved }) {
                Text(if (moved) "Volver al centro" else "Mover y agrandar")
            }
        }
    }
}
