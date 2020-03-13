package com.eddystudio.shuttletracker.ui.realtimemap

import com.eddystudio.shuttletracker.data.model.RouteStop

interface RouteStopClickListener {
  fun OnRouteStopClicked(routeStop: RouteStop)
}