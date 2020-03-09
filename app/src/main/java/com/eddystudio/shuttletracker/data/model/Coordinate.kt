package com.eddystudio.shuttletracker.data.model


import com.google.gson.annotations.SerializedName

data class Coordinate(
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double
)