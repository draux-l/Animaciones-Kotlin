package com.example.lab13_animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TapTheCircleGame() {

    var score by remember { mutableStateOf(0) }
    var circleVisible by remember { mutableStateOf(true) }
    var isGameOver by remember { mutableStateOf(false) }
    var isVictory by remember { mutableStateOf(false) }

    // Posición aleatoria
    var posX by remember { mutableStateOf(100.dp) }
    var posY by remember { mutableStateOf(200.dp) }

    // Tamaño del círculo que se reduce con el tiempo
    var circleSize by remember { mutableStateOf(120.dp) }

    val animatedSize by animateDpAsState(
        targetValue = circleSize,
        animationSpec = tween(1000),
        label = "circleSize"
    )

    // Color del círculo
    val circleColor by animateColorAsState(
        targetValue = Color(
            Random.nextFloat(),
            Random.nextFloat(),
            Random.nextFloat(),
            1f
        ),
        tween(600),
        label = "circleColor"
    )

    val scope = rememberCoroutineScope()

    // Cada ronda
    LaunchedEffect(score, isGameOver, isVictory) {
        if (!isGameOver && !isVictory) {

            // Reset tamaño
            circleSize = 120.dp

            // Posición aleatoria
            posX = Random.nextInt(20, 250).dp
            posY = Random.nextInt(80, 500).dp

            // Círculo visible
            circleVisible = true

            // Reduce el tamaño hasta desaparecer
            while (circleSize > 10.dp && !isGameOver && !isVictory) {
                delay(80)
                circleSize -= 10.dp
            }

            // Si desaparece sin tocarlo
            if (!isVictory && circleSize <= 10.dp) {
                isGameOver = true
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1D))
            .padding(16.dp)
    ) {

        // Puntaje
        Text(
            "Puntaje: $score / 10",
            color = Color.White,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Círculo
        AnimatedVisibility(
            visible = circleVisible && !isGameOver && !isVictory,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .size(animatedSize)
                    .offset(posX, posY)
                    .background(circleColor, CircleShape)
                    .clickable {
                        if (!isGameOver && !isVictory) {
                            score++

                            if (score >= 10) {
                                isVictory = true
                            } else {
                                // iniciar nueva ronda
                                circleVisible = false
                            }
                        }
                    }
            )
        }

        // Game Over
        AnimatedVisibility(isGameOver) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(
                    text = "GAME OVER\nFallaste el toque",
                    color = Color.Red
                )
            }
        }

        // Victoria
        AnimatedVisibility(isVictory) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(
                    text = "✨ ¡Victoria! ✨\nTocaste 10 círculos",
                    color = Color.Green
                )
            }
        }
    }
}
