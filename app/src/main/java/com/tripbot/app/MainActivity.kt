package com.tripbot.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tripbot.app.ui.screens.CreateTripScreen
import com.tripbot.app.ui.screens.TripDetailScreen
import com.tripbot.app.ui.screens.TripListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "trips"
                    ) {
                        composable("trips") {
                            TripListScreen(
                                onTripClick = { tripId ->
                                    navController.navigate("trip/$tripId")
                                },
                                onAddTrip = {
                                    navController.navigate("create")
                                }
                            )
                        }
                        composable(
                            route = "trip/{tripId}",
                            arguments = listOf(navArgument("tripId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val tripId = backStackEntry.arguments?.getInt("tripId") ?: 0
                            TripDetailScreen(
                                tripId = tripId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("create") {
                            CreateTripScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }

        // Проверка обновлений при запуске
        UpdateChecker.checkForUpdate(this)
    }
}
