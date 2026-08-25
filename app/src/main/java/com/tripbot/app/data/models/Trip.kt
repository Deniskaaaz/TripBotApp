package com.tripbot.app.data.models

import com.google.gson.annotations.SerializedName

data class Trip(
    @SerializedName("id") val id: Int,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("city") val city: String,
    @SerializedName("start_point") val startPoint: String,
    @SerializedName("end_point") val endPoint: String,
    @SerializedName("total_km") val totalKm: Double,
    @SerializedName("total_duration_sec") val totalDurationSec: Int,
    @SerializedName("total_pause_sec") val totalPauseSec: Int,
    @SerializedName("total_cost") val totalCost: Double,
    @SerializedName("points") val points: List<String>,
    @SerializedName("username") val username: String? = null
)
