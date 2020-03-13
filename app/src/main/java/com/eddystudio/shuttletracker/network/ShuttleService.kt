package com.studio.eddy.myapplication.network

import com.eddystudio.shuttletracker.data.model.BusArrival
import com.eddystudio.shuttletracker.data.model.RouteStop
import com.eddystudio.shuttletracker.data.model.RoutVehicle
import com.eddystudio.shuttletracker.data.model.Route
import com.studio.eddy.myapplication.data.RoutWayPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ShuttleService {

  @GET("Route/{waypoint}/Waypoints/")
  fun getWayPoint(@Path("waypoint") waypoint: String): Call<List<List<RoutWayPoint>>>

  @GET("Region/0/Routes")
  fun getAllRoute(): Call<List<Route>>

  @GET("Route/{id}/Direction/10/Stops")
  fun getStopsForRout(@Path("id") id: String): Call<List<RouteStop>>

  @GET("Route/{id}/Vehicles")
  fun getRoutVehichles(@Path("id") id: String): Call<List<RoutVehicle>>

  @GET("Stop/{stopId}/Arrivals")
  fun getArrivalByStopId(@Path("stopId") stopId: String): Call<List<BusArrival>>

  @GET("Vehicle/{shuttleId}/Arrivals?customerID=2")
  fun getShuttleNextStop(@Path("shuttleId") shuttleId : String)

  @GET("Stop/{stopId}/Routes")
  fun getRoutesByStopId(@Path("stopId") stopId: String): Call<List<Route>>
}