package com.studio.eddy.myapplication.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eddystudio.shuttletracker.data.model.BusArrival
import com.eddystudio.shuttletracker.data.model.RouteStop
import com.eddystudio.shuttletracker.data.model.RoutVehicle
import com.eddystudio.shuttletracker.data.model.Route
import com.studio.eddy.myapplication.data.RoutWayPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val shuttleService: ShuttleService) {

  fun getWayPoint(routId: String): LiveData<Response<List<List<RoutWayPoint>>>> {
    return MutableLiveData<Response<List<List<RoutWayPoint>>>>().apply {
      shuttleService.getWayPoint(routId)
          .enqueue(object : Callback<List<List<RoutWayPoint>>> {
            override fun onFailure(
              call: Call<List<List<RoutWayPoint>>>,
              t: Throwable
            ) {
              Log.e("repository", "get way point error", t)
            }

            override fun onResponse(
              call: Call<List<List<RoutWayPoint>>>,
              response: Response<List<List<RoutWayPoint>>>
            ) {
              value = response
            }

          })
    }
  }

  fun getAllRoutes(): LiveData<Response<List<Route>>> {
    return MutableLiveData<Response<List<Route>>>().apply {
      shuttleService.getAllRoute()
          .enqueue(object : Callback<List<Route>> {
            override fun onFailure(
              call: Call<List<Route>>,
              t: Throwable
            ) {
              Log.e("repository", "get all routes error", t)
            }

            override fun onResponse(
              call: Call<List<Route>>,
              response: Response<List<Route>>
            ) {
              value = response
            }

          })
    }
  }

  fun getRouteStops(id: String): LiveData<Response<List<RouteStop>>> {
    return MutableLiveData<Response<List<RouteStop>>>().apply {
      shuttleService.getStopsForRout(id)
          .enqueue(object : Callback<List<RouteStop>> {
            override fun onFailure(
              call: Call<List<RouteStop>>,
              t: Throwable
            ) {
              Log.e("repository", "get stops error", t)
            }

            override fun onResponse(
              call: Call<List<RouteStop>>,
              response: Response<List<RouteStop>>
            ) {
              value = response
            }

          })
    }
  }

  fun getRoutVehicles(id: String): LiveData<Response<List<RoutVehicle>>> {
    return MutableLiveData<Response<List<RoutVehicle>>>().apply {
      shuttleService.getRoutVehichles(id)
          .enqueue(object : Callback<List<RoutVehicle>> {
            override fun onFailure(
              call: Call<List<RoutVehicle>>,
              t: Throwable
            ) {
              Log.e("repository", "get rout vehicle error", t)
            }

            override fun onResponse(
              call: Call<List<RoutVehicle>>,
              response: Response<List<RoutVehicle>>
            ) {
              value = response
            }

          })
    }
  }

  fun getArrivalByStopId(id: String): LiveData<Response<List<BusArrival>>> {
    return MutableLiveData<Response<List<BusArrival>>>().apply {
      shuttleService.getArrivalByStopId(id)
          .enqueue(object : Callback<List<BusArrival>> {
            override fun onFailure(
              call: Call<List<BusArrival>>,
              t: Throwable
            ) {
              Log.e("repository", "get bus arrival error", t)
            }

            override fun onResponse(
              call: Call<List<BusArrival>>,
              response: Response<List<BusArrival>>
            ) {
              value = response
            }
          })
    }
  }

}