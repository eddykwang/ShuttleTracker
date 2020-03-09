package com.eddystudio.shuttletracker.data.model


import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("ArrivalsEnabled")
    val arrivalsEnabled: Boolean,
    @SerializedName("ArrivalsShowVehicleNames")
    val arrivalsShowVehicleNames: Boolean,
    @SerializedName("BackwardDirectionName")
    val backwardDirectionName: String,
    @SerializedName("Color")
    val color: String,
    @SerializedName("CustomerID")
    val customerID: Int,
    @SerializedName("DirectionStops")
    val directionStops: Any?,
    @SerializedName("DisplayName")
    val displayName: String,
    @SerializedName("ForwardDirectionName")
    val forwardDirectionName: String,
    @SerializedName("ID")
    val iD: Int,
    @SerializedName("IsHeadway")
    val isHeadway: Boolean,
    @SerializedName("Name")
    val name: String,
    @SerializedName("NumberOfVehicles")
    val numberOfVehicles: Int,
    @SerializedName("Patterns")
    val patterns: Any?,
    @SerializedName("Points")
    val points: Any?,
    @SerializedName("RegionIDs")
    val regionIDs: List<Any>,
    @SerializedName("ShortName")
    val shortName: String,
    @SerializedName("ShowLine")
    val showLine: Boolean,
    @SerializedName("TextColor")
    val textColor: String
)