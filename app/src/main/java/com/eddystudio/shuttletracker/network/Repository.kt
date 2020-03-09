package com.studio.eddy.myapplication.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eddystudio.shuttletracker.data.model.RoutStop
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

  val wayPointLiveData: MutableLiveData<List<RoutWayPoint>> = MutableLiveData()
  fun getWayPoint(routId: String) {
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
            wayPointLiveData.value = (response.body()
                ?.get(0))
          }

        })
  }

  fun getAllRoutes(): LiveData<Response<List<Route>>> {
    return MutableLiveData<Response<List<Route>>>().apply {
      shuttleService.getRoutes()
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

  fun getRouteStops(id: String): LiveData<Response<List<RoutStop>>> {
    return MutableLiveData<Response<List<RoutStop>>>().apply {
      shuttleService.getStopsForRout(id)
          .enqueue(object : Callback<List<RoutStop>> {
            override fun onFailure(
              call: Call<List<RoutStop>>,
              t: Throwable
            ) {
              Log.e("repository", "get stops error", t)
            }

            override fun onResponse(
              call: Call<List<RoutStop>>,
              response: Response<List<RoutStop>>
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

}