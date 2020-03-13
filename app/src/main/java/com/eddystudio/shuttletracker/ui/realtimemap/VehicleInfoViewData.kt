package com.eddystudio.shuttletracker.ui.realtimemap

import androidx.lifecycle.MutableLiveData
import com.eddystudio.shuttletracker.data.model.RoutVehicle

class VehicleInfoViewData(routeVehicle: RoutVehicle) {
  val vehicleName = MutableLiveData<String>()
  val vehicleId = MutableLiveData<String>()
  val speed = MutableLiveData<String>()

  init {
    vehicleName.value = routeVehicle.name
    vehicleId.value = routeVehicle.iD.toString()
    speed.value = routeVehicle.speed.toString()
  }
}