package com.tripbot.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tripbot.app.data.models.Trip
import com.tripbot.app.ui.viewmodels.TripsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
    onTripClick: (Int) -> Unit,
    onAddTrip: () -> Unit,
    viewModel: TripsViewModel = viewModel()
) {
    val trips by viewModel.trips
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    Scaffold(
        topBar = { TopAppBar(title = { Text("Мои поездки") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTrip) {
                Icon(Icons.Default.Add, contentDescription = "Новая поездка")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text("Ошибка: $error", modifier = Modifier.align(Alignment.Center))
                trips.isEmpty() -> Text("Нет поездок", modifier = Modifier.align(Alignment.Center))
                else -> LazyColumn {
                    items(trips) { trip ->
                        TripItem(trip) { onTripClick(trip.id) }
                    }
                }
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${trip.start_point} → ${trip.end_point}", style = MaterialTheme.typography.titleMedium)
            Text(text = "📏 ${trip.total_km} км | 💰 ${trip.total_cost} руб", style = MaterialTheme.typography.bodyMedium)
            Text(text = trip.timestamp, style = MaterialTheme.typography.bodySmall)
        }
    }
}
