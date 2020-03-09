package com.studio.eddy.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.eddystudio.shuttletracker.data.model.RoutStop
import com.eddystudio.shuttletracker.data.model.RoutVehicle
import com.eddystudio.shuttletracker.data.model.Route
import com.google.android.gms.maps.model.LatLng
import com.studio.eddy.myapplication.network.Repository
import javax.inject.Inject
import kotlin.collections.ArrayList

class RealTimeMapViewModel @Inject constructor(val repository: Repository) : ViewModel() {

  private val wayPoint = MutableLiveData<List<LatLng>>()

  fun getWayPointLiveData(id: String): LiveData<List<LatLng>> {
    val latLongList: ArrayList<LatLng> = ArrayList()

    repository.getWayPoint(id)

    return Transformations.map(repository.wayPointLiveData) {
      it.map { LatLng(it.latitude, it.longitude) }
    }
  }

  fun getAllRoutes(): LiveData<List<Route>> =
    Transformations.map(repository.getAllRoutes()) { it.body() }

  fun getRoutStops(id: String): LiveData<List<RoutStop>> =
    Transformations.map(repository.getRouteStops(id)) { it.body() }

  fun getRutVehicles(id: String): LiveData<List<RoutVehicle>> =
    Transformations.map(repository.getRoutVehicles(id)) { it.body() }
}