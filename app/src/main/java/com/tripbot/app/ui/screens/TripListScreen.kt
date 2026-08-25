package com.tripbot.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tripbot.app.data.models.Trip
import com.tripbot.app.ui.viewmodels.TripListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
    onTripClick: (Int) -> Unit,
    onAddTrip: () -> Unit,
    viewModel: TripListViewModel = viewModel()
) {
    val trips by viewModel.trips.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Мои поездки") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTrip) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (trips.isEmpty()) {
                    Text(
                        text = "Пока нет поездок",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(trips) { trip ->
                            TripItem(trip = trip, onClick = { onTripClick(trip.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TripItem(trip: Trip, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${trip.city}: ${trip.startPoint} → ${trip.endPoint}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${trip.totalKm} км · ${trip.totalDurationSec / 60} мин")
            if (trip.totalPauseSec > 0) {
                Text(
                    text = "Пауза: ${trip.totalPauseSec / 60} мин",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
