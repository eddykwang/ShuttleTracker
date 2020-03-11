package com.eddystudio.shuttletracker.data.model


import com.google.gson.annotations.SerializedName

data class BusArrival(
    @SerializedName("Arrivals")
    val arrivals: List<Arrival>,
    @SerializedName("RouteID")
    val routeID: Int
)