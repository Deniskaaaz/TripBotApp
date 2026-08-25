package com.tripbot.app.data.api

import com.tripbot.app.data.models.RouteRequest
import com.tripbot.app.data.models.RouteResponse
import com.tripbot.app.data.models.SaveTripRequest
import com.tripbot.app.data.models.Trip
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("trips/{user_id}")
    suspend fun getTrips(@Path("user_id") userId: Int): List<Trip>

    @POST("route")
    suspend fun calculateRoute(@Body request: RouteRequest): RouteResponse

    @POST("save_trip")
    suspend fun saveTrip(@Body request: SaveTripRequest): retrofit2.Response<Unit>
}
