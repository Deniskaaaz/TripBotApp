package com.tripbot.app.data.repository

import com.tripbot.app.data.api.RetrofitClient
import com.tripbot.app.data.models.RouteRequest
import com.tripbot.app.data.models.RouteResponse
import com.tripbot.app.data.models.SaveTripRequest
import com.tripbot.app.data.models.Trip

class TripRepository {
    suspend fun getTrips(userId: Int): List<Trip> = RetrofitClient.apiService.getTrips(userId)

    suspend fun calculateRoute(origin: String, destination: String, city: String): RouteResponse {
        return RetrofitClient.apiService.calculateRoute(RouteRequest(origin, destination, city))
    }

    suspend fun saveTrip(request: SaveTripRequest) {
        RetrofitClient.apiService.saveTrip(request)
    }
}
