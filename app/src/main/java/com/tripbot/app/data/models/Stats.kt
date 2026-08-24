package com.tripbot.app.data.models

data class Stats(
    val trips_count: Int,
    val total_km: Double,
    val total_duration: Int,
    val total_cost: Double,
    val avg_km: Double
)
