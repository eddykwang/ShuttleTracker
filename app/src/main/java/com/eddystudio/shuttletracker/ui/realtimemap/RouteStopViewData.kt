package com.eddystudio.shuttletracker.ui.realtimemap

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.eddystudio.shuttletracker.data.model.BusArrival
import com.eddystudio.shuttletracker.data.model.Route
import com.eddystudio.shuttletracker.data.model.RouteStop

class RouteStopViewData(
  val routeStop: RouteStop,
  private var routStopClickListener: RouteStopClickListener
) {
  val name = ObservableField<String>()
  val arrivalDetailVisibility = ObservableBoolean(false)
  val secondRowVisibility = ObservableBoolean(false)
  val errorMessageVisibility = ObservableBoolean(false)

  val id1 = ObservableField<String>()
  val id2 = ObservableField<String>()
  val time1 = ObservableField<String>()
  val time2 = ObservableField<String>()

  init {
    name.set(routeStop.name)
  }

  fun onStopClicked() {
    routStopClickListener.OnRouteStopClicked(routeStop)
  }

  fun setArrivalInfo(
    arrival: List<BusArrival>,
    selectedRoute: Route
  ) {

    if (arrival.isEmpty()) {
      arrivalDetailVisibility.set(false)
      errorMessageVisibility.set(true)
    }

    arrival.forEach { busArrival ->
      if (busArrival.routeID == selectedRoute.iD) {
        if (busArrival.arrivals.isNotEmpty()) {
          arrivalDetailVisibility.set(true)
          errorMessageVisibility.set(false)
          id1.set(busArrival.arrivals[0].busName)
          time1.set(busArrival.arrivals[0].arriveTime)
          if (busArrival.arrivals.size > 1) {
            secondRowVisibility.set(true)
            id2.set(busArrival.arrivals[1].busName)
            time2.set(busArrival.arrivals[1].arriveTime)
          } else {
            secondRowVisibility.set(false)
          }
        }
      }
    }
  }

  fun hideDetails() {
    arrivalDetailVisibility.set(false)
    errorMessageVisibility.set(false)
  }

  fun canShowDetail() :Boolean {
    return !arrivalDetailVisibility.get() && !errorMessageVisibility.get()
  }

}