package com.tripbot.app.data.repository

import com.tripbot.app.data.api.RetrofitClient
import com.tripbot.app.data.models.Trip

class TripRepository {
    suspend fun getTrips(userId: Int): List<Trip> {
        return RetrofitClient.apiService.getTrips(userId)
    }
}
