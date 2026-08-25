package com.tripbot.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    onBack: () -> Unit
) {
    var startPoint by remember { mutableStateOf("") }
    var endPoint by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новая поездка") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = startPoint,
                onValueChange = { startPoint = it },
                label = { Text("Начальная точка") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = endPoint,
                onValueChange = { endPoint = it },
                label = { Text("Конечная точка") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    // Пока просто показываем введённые данные
                    resultText = "Маршрут: $startPoint → $endPoint\n(расчёт появится позже)"
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Рассчитать маршрут")
            }

            if (resultText.isNotEmpty()) {
                Text(
                    text = resultText,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
