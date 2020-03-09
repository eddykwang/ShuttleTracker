package com.studio.eddy.myapplication.network

import com.eddystudio.shuttletracker.data.model.RoutStop
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
  fun getRoutes(): Call<List<Route>>

  @GET("Route/{id}/Direction/10/Stops")
  fun getStopsForRout(@Path("id") id: String): Call<List<RoutStop>>

  @GET("Route/{id}/Vehicles")
  fun getRoutVehichles(@Path("id") id: String): Call<List<RoutVehicle>>
}