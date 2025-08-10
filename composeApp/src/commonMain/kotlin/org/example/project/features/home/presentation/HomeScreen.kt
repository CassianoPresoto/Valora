package org.example.project.features.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import valora.composeapp.generated.resources.Res
import valora.composeapp.generated.resources.compose_multiplatform
import org.example.project.Greeting

object HomeScreen {
    @Composable
    fun Content(innerPadding: PaddingValues) {
        val greeting = remember { Greeting().greet() }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .safeContentPadding()
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Bem-vindo", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 24.dp))
            Image(painterResource(Res.drawable.compose_multiplatform), null)
            Text("Compose: $greeting")
            var showMore by remember { mutableStateOf(false) }
            Button(onClick = { showMore = !showMore }, modifier = Modifier.padding(top = 16.dp)) {
                Text(if (showMore) "Ocultar" else "Mostrar")
            }
            AnimatedVisibility(showMore) {
                Text("Conteúdo adicional da Home")
            }
        }
    }
}
