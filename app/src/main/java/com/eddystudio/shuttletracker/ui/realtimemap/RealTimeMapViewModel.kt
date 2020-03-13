package com.studio.eddy.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.eddystudio.shuttletracker.data.model.BusArrival
import com.eddystudio.shuttletracker.data.model.RouteStop
import com.eddystudio.shuttletracker.data.model.RoutVehicle
import com.eddystudio.shuttletracker.data.model.Route
import com.google.android.gms.maps.model.LatLng
import com.studio.eddy.myapplication.network.Repository
import javax.inject.Inject

class RealTimeMapViewModel @Inject constructor(val repository: Repository) : ViewModel() {

  fun getWayPointLiveData(id: String): LiveData<List<LatLng>> {
    return Transformations.map(repository.getWayPoint(id)) { response ->
      response.body()
          ?.get(0)
          ?.map {
            LatLng(it.latitude, it.longitude)
          }
    }
  }

  fun getAllRoutes(): LiveData<List<Route>> =
    Transformations.map(repository.getAllRoutes()) { it.body() }

  fun getRouteStops(id: String): LiveData<List<RouteStop>> =
    Transformations.map(repository.getRouteStops(id)) { it.body() }

  fun getRouteVehicles(id: String): LiveData<List<RoutVehicle>> =
    Transformations.map(repository.getRoutVehicles(id)) { it.body() }

  fun getBusArrivalByStopId(id: String): LiveData<List<BusArrival>> =
    Transformations.map(repository.getArrivalByStopId(id)) { it.body() }
}