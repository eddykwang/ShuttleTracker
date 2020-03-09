package com.eddystudio.shuttletracker.data.model


import com.eddystudio.shuttletracker.data.model.Coordinate
import com.google.gson.annotations.SerializedName

data class RoutVehicle(
    @SerializedName("APCPercentage")
    val aPCPercentage: Int,
    @SerializedName("Coordinate")
    val coordinate: Coordinate,
    @SerializedName("DoorStatus")
    val doorStatus: Int,
    @SerializedName("HasAPC")
    val hasAPC: Boolean,
    @SerializedName("Heading")
    val heading: String,
    @SerializedName("ID")
    val iD: Int,
    @SerializedName("IconPrefix")
    val iconPrefix: String,
    @SerializedName("Latitude")
    val latitude: Double,
    @SerializedName("Longitude")
    val longitude: Double,
    @SerializedName("Name")
    val name: String,
    @SerializedName("PatternId")
    val patternId: Int,
    @SerializedName("RouteId")
    val routeId: Int,
    @SerializedName("Speed")
    val speed: Int,
    @SerializedName("Updated")
    val updated: String,
    @SerializedName("UpdatedAgo")
    val updatedAgo: String
)