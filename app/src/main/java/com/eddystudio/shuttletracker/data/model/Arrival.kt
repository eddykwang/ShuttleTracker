package com.eddystudio.shuttletracker.data.model


import com.google.gson.annotations.SerializedName

data class Arrival(
    @SerializedName("ArriveTime")
    val arriveTime: String,
    @SerializedName("BusName")
    val busName: String,
    @SerializedName("Direction")
    val direction: Int,
    @SerializedName("IsLastStop")
    val isLastStop: Boolean,
    @SerializedName("IsLayover")
    val isLayover: Boolean,
    @SerializedName("Minutes")
    val minutes: Int,
    @SerializedName("OnBreak")
    val onBreak: Boolean,
    @SerializedName("RouteID")
    val routeID: Int,
    @SerializedName("RouteId")
    val routeId: Int,
    @SerializedName("RouteName")
    val routeName: String,
    @SerializedName("Rules")
    val rules: List<Any>,
    @SerializedName("SchedulePrediction")
    val schedulePrediction: Boolean,
    @SerializedName("ScheduledArriveTime")
    val scheduledArriveTime: Any?,
    @SerializedName("ScheduledMinutes")
    val scheduledMinutes: Int,
    @SerializedName("ScheduledTime")
    val scheduledTime: Any?,
    @SerializedName("SecondsToArrival")
    val secondsToArrival: Double,
    @SerializedName("StopID")
    val stopID: Int,
    @SerializedName("StopId")
    val stopId: Int,
    @SerializedName("Time")
    val time: String,
    @SerializedName("TripId")
    val tripId: Any?,
    @SerializedName("VehicleID")
    val vehicleID: Int,
    @SerializedName("VehicleId")
    val vehicleId: Int,
    @SerializedName("VehicleName")
    val vehicleName: String
)