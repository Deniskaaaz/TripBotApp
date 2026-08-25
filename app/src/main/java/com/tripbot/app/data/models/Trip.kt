package com.tripbot.app.data.models

import com.google.gson.annotations.SerializedName

data class Trip(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("start_point") val startPoint: String,
    @SerializedName("end_point") val endPoint: String,
    @SerializedName("distance_km") val distanceKm: Double,
    @SerializedName("duration_min") val durationMin: Int,
    @SerializedName("pause_min") val pauseMin: Int? = 0,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("points") val points: String? = null
)
