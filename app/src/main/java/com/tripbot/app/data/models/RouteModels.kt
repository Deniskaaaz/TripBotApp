package com.tripbot.app.data.models

import com.google.gson.annotations.SerializedName

data class RouteRequest(
    @SerializedName("origin") val origin: String,
    @SerializedName("destination") val destination: String,
    @SerializedName("city") val city: String
)

data class RouteResponse(
    @SerializedName("distance_km") val distanceKm: Double,
    @SerializedName("duration_sec") val durationSec: Int,
    @SerializedName("origin_coords") val originCoords: List<Double>,
    @SerializedName("dest_coords") val destCoords: List<Double>,
    @SerializedName("map_link") val mapLink: String
)

data class SaveTripRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("city") val city: String,
    @SerializedName("start_point") val startPoint: String,
    @SerializedName("end_point") val endPoint: String,
    @SerializedName("total_km") val totalKm: Double,
    @SerializedName("total_duration_sec") val totalDurationSec: Int,
    @SerializedName("total_pause_sec") val totalPauseSec: Int = 0,
    @SerializedName("total_cost") val totalCost: Double = 0.0,
    @SerializedName("points") val points: List<String> = emptyList(),
    @SerializedName("username") val username: String? = null
)
