package com.tripbot.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.tripbot.app.ui.screens.TripListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    TripListScreen(
                        onTripClick = { tripId ->
                            // Здесь можно открыть детали поездки
                            // Пока заглушка
                        },
                        onAddTrip = {
                            // Действие при нажатии "Добавить поездку"
                            // Пока заглушка
                        }
                    )
                }
            }
        }
    }
}
