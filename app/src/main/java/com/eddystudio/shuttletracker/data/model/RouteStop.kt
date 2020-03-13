package com.eddystudio.shuttletracker.data.model


import com.google.gson.annotations.SerializedName

data class RouteStop(
    @SerializedName("ID")
    val iD: Int,
    @SerializedName("Image")
    val image: String,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double,
    @SerializedName("Name")
    val name: String,
    @SerializedName("RtpiNumber")
    val rtpiNumber: Int,
    @SerializedName("ShowLabel")
    val showLabel: Boolean,
    @SerializedName("ShowStopRtpiNumberLabel")
    val showStopRtpiNumberLabel: Boolean,
    @SerializedName("ShowVehicleName")
    val showVehicleName: Boolean
)