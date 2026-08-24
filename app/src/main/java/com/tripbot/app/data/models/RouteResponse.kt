package com.tripbot.app.data.models

data class RouteResponse(
    val distance_km: Double,
    val duration_sec: Int,
    val origin_coords: List<Double>,
    val dest_coords: List<Double>,
    val map_link: String
)
