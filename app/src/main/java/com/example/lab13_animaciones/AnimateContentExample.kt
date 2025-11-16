package com.example.lab13_animaciones

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed class ScreenState {
    object Loading : ScreenState()
    object Content : ScreenState()
    object Error : ScreenState()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentExample() {
    var uiState by remember { mutableStateOf<ScreenState>(ScreenState.Loading) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Controles para cambiar de estado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { uiState = ScreenState.Loading }) {
                Text("Cargando")
            }
            Button(onClick = { uiState = ScreenState.Content }) {
                Text("Contenido")
            }
            Button(onClick = { uiState = ScreenState.Error }) {
                Text("Error")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = uiState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) with
                            fadeOut(animationSpec = tween(400))
                },
                label = "ScreenStateAnimation"
            ) { state ->
                when (state) {
                    ScreenState.Loading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Cargando datos, por favor espera...")
                        }
                    }

                    ScreenState.Content -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Contenido cargado correctamente")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Aquí iría la información principal de la pantalla.")
                        }
                    }

                    ScreenState.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Ocurrió un error al cargar los datos.")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Intenta nuevamente o revisa tu conexión.")
                        }
                    }
                }
            }
        }
    }
}
