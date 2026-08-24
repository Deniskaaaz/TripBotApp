package com.tripbot.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tripbot.app.data.api.RetrofitInstance
import com.tripbot.app.data.models.RouteRequest
import kotlinx.coroutines.launch

@Composable
fun CreateTripScreen(onTripCreated: () -> Unit) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Нижний Новгород") }
    var resultText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Новая поездка") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = origin,
                onValueChange = { origin = it },
                label = { Text("Начальная точка") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Конечная точка") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Город") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val response = RetrofitInstance.api.calculateRoute(
                                RouteRequest(origin, destination, city)
                            )
                            resultText = "Расстояние: ${response.distance_km} км\nВремя: ${response.duration_sec / 60} мин"
                        } catch (e: Exception) {
                            resultText = "Ошибка: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Рассчитать")
            }
            if (resultText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(resultText, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onTripCreated,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Сохранить поездку")
            }
        }
    }
}
