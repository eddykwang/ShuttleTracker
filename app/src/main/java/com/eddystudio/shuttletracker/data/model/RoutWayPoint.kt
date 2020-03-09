package com.studio.eddy.myapplication.data


import com.google.gson.annotations.SerializedName

data class RoutWayPoint(
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double
)