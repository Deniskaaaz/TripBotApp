package com.tripbot.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.tripbot.app.data.models.SaveTripRequest
import com.tripbot.app.data.repository.TripRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    onBack: () -> Unit
) {
    var city by remember { mutableStateOf("") }
    var startPoint by remember { mutableStateOf("") }
    var endPoint by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var calculatedDistance by remember { mutableStateOf(0.0) }
    var calculatedDuration by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()
    val repository = remember { TripRepository() }

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
                value = city,
                onValueChange = { city = it },
                label = { Text("Город") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = startPoint,
                onValueChange = { startPoint = it },
                label = { Text("Начальная точка (адрес)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = endPoint,
                onValueChange = { endPoint = it },
                label = { Text("Конечная точка (адрес)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val response = repository.calculateRoute(
                                origin = startPoint,
                                destination = endPoint,
                                city = city
                            )
                            calculatedDistance = response.distanceKm
                            calculatedDuration = response.durationSec
                            resultText = "Дистанция: ${response.distanceKm} км, Время: ${response.durationSec / 60} мин"
                        } catch (e: Exception) {
                            resultText = "Ошибка расчёта: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Рассчитать маршрут")
            }

            if (resultText.isNotEmpty()) {
                Text(text = resultText, modifier = Modifier.fillMaxWidth())
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val request = SaveTripRequest(
                                userId = 1, // временно
                                city = city,
                                startPoint = startPoint,
                                endPoint = endPoint,
                                totalKm = calculatedDistance,
                                totalDurationSec = calculatedDuration,
                                totalPauseSec = 0,
                                totalCost = 0.0,
                                points = emptyList(),
                                username = null
                            )
                            repository.saveTrip(request)
                            resultText = "Поездка сохранена!"
                        } catch (e: Exception) {
                            resultText = "Ошибка сохранения: ${e.message}"
                        }
                    }
                },
                enabled = calculatedDistance > 0,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Сохранить поездку")
            }
        }
    }
}
