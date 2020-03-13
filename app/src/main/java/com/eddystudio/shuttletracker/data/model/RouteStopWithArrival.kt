package com.eddystudio.shuttletracker.data.model

data class RouteStopWithArrival(val routeStop: RouteStop, val arrival: List<BusArrival>?) {
}