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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EscapeDelDragonGame() {

    // Estados principales
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var isVictory by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Posición jugador
    var playerY by remember { mutableStateOf(0.dp) }
    val animatedPlayerY by animateDpAsState(
        targetValue = playerY,
        animationSpec = tween(250),
        label = "playerAnim"
    )

    // Posición dragón
    var dragonX by remember { mutableStateOf(500.dp) } // empieza fuera de pantalla
    var dragonY = 0.dp
    val animatedDragonX by animateDpAsState(
        targetValue = dragonX,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "dragonAnim"
    )

    // Color del dragón
    val dragonColor by animateColorAsState(
        targetValue = if (Random.nextBoolean()) Color.Red else Color(0xFFFF9800),
        animationSpec = tween(600),
        label = "dragonColor"
    )

    // Golpe vibración
    val shake = remember { Animatable(0f) }

    if (isGameOver) {
        LaunchedEffect(isGameOver) {
            shake.animateTo(
                1f,
                animationSpec = repeatable(
                    iterations = 6,
                    tween(80),
                    RepeatMode.Reverse
                )
            )
        }
    }

    // Layout Principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF202020))
            .padding(16.dp)
    ) {

        Text(
            text = "Esquives: $score / 10",
            color = Color.White,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // PLAYER ***
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 40.dp + (shake.value * 20).dp)
                .offset(y = animatedPlayerY)
                .size(70.dp)
                .background(Color(0xFF4FC3F7), CircleShape)
                .clickable {
                    if (!isGameOver && !isVictory) {
                        playerY = (-160).dp
                        scope.launch {
                            delay(300)
                            playerY = 0.dp
                        }
                    }
                }
        )

        // DRAGON ***
        AnimatedVisibility(!isGameOver && !isVictory) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = animatedDragonX, y = dragonY)
                    .size(100.dp)
                    .background(dragonColor, CircleShape)
            )
        }

        // LÓGICA DEL DRAGÓN ***
        LaunchedEffect(score, isGameOver, isVictory) {
            if (!isGameOver && !isVictory) {

                delay(1000) // tiempo antes del ataque

                // Dragón viene desde la derecha
                dragonX = 500.dp
                dragonX = 0.dp

                // Comprobar golpe
                if (playerY > -80.dp) {
                    isGameOver = true
                } else {
                    score++
                    if (score >= 10) {
                        isVictory = true
                    }
                }
            }
        }

        // GAME OVER
        AnimatedVisibility(isGameOver) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "GAME OVER\nEl dragón te alcanzó",
                    color = Color.Red,
                    modifier = Modifier.scale(1.4f)
                )
            }
        }

        // VICTORIA
        AnimatedVisibility(isVictory) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "✨ ¡VICTORIA! ✨\nEsquivaste al dragón",
                    color = Color(0xFF00E676),
                    modifier = Modifier.scale(1.3f)
                )
            }
        }
    }
}
