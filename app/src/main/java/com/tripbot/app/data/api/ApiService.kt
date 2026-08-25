package com.tripbot.app.data.api

import com.tripbot.app.data.models.Trip
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("trips/{user_id}")
    suspend fun getTrips(@Path("user_id") userId: Int): List<Trip>
}
