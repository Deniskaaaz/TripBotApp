package com.tripbot.app.data.api

import com.tripbot.app.data.models.RouteRequest
import com.tripbot.app.data.models.RouteResponse
import com.tripbot.app.data.models.Stats
import com.tripbot.app.data.models.Trip
import retrofit2.http.*

interface ApiService {
    @GET("trips/{user_id}")
    suspend fun getTrips(
        @Path("user_id") userId: Int,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): List<Trip>

    @GET("stats/{user_id}")
    suspend fun getStats(@Path("user_id") userId: Int): Stats

    @POST("route")
    suspend fun calculateRoute(@Body request: RouteRequest): RouteResponse
}
