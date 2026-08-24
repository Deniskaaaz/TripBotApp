package com.tripbot.app.data.models

data class Trip(
    val id: Int,
    val timestamp: String,
    val city: String,
    val start_point: String,
    val end_point: String,
    val total_km: Double,
    val total_duration_sec: Int,
    val total_pause_sec: Int,
    val total_cost: Double,
    val points: List<String> = emptyList(),
    val username: String? = null
)
