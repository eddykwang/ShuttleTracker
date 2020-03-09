package com.studio.eddy.myapplication.ui.home

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eddystudio.shuttletracker.R
import com.eddystudio.shuttletracker.data.MySharedPreference
import com.eddystudio.shuttletracker.data.model.Route
import com.eddystudio.shuttletracker.util.Util
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.studio.eddy.myapplication.dagger.MyApplication
import kotlinx.android.synthetic.main.fragment_real_time_map.get_my_location_bt
import kotlinx.android.synthetic.main.fragment_real_time_map.rout_search_bar
import java.util.Collections
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class RealTimeMapFragment : Fragment(), OnMapReadyCallback {

  @Inject lateinit var realTimeMapViewModelFactory: RealTimeMapViewModelFactory
  @Inject lateinit var sharedPreference: MySharedPreference
  private lateinit var realTimeMapViewModel: RealTimeMapViewModel
  private lateinit var mMap: GoogleMap
  private lateinit var mapFragment: SupportMapFragment
  private lateinit var routeList: List<Route>
  private val vehicleMarks = Collections.synchronizedList<Marker>(ArrayList())
  private var isMarkerRotating = false
  private lateinit var vehicleTimerTask: TimerTask
  private lateinit var myLocation: LatLng

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    MyApplication.component.inject(this)
    realTimeMapViewModel = ViewModelProvider(this, realTimeMapViewModelFactory)
        .get(RealTimeMapViewModel::class.java)
    return inflater.inflate(R.layout.fragment_real_time_map, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
    mapFragment.getMapAsync(this)
    initViews()
    setupObservers()
  }

  private fun initViews() {
    rout_search_bar.apply {
      this.lifecycleOwner = viewLifecycleOwner
      this.setOnSpinnerItemSelectedListener(object : OnSpinnerItemSelectedListener<String> {
        override fun onItemSelected(
          position: Int,
          item: String
        ) {
          Log.d(TAG, "$item selected, pos = $position")
          val selected = routeList[position]
          clearAllVehicleMarkers()
          if (::vehicleTimerTask.isInitialized) {
            vehicleTimerTask.cancel()
          }
          drawMapResrouce(selected)
          sharedPreference.storeLastSelectedRout(routeList[position].iD)
        }

      })
    }

    get_my_location_bt.setOnClickListener {
      if (::mMap.isInitialized && ::myLocation.isInitialized) {
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                myLocation,
                15f
            )
        )
      }
    }
  }

  private fun clearAllVehicleMarkers() {
    for (maker in vehicleMarks) {
      maker.remove()
    }
    vehicleMarks.clear()
  }

  private fun setupObservers() {
    setupAllRoutes()
  }

  private fun setupAllRoutes() {
    realTimeMapViewModel.getAllRoutes()
        .observe(viewLifecycleOwner, Observer { list ->
          routeList = list
          rout_search_bar.setItems(routeList.map {
            it.name
          })

          if (sharedPreference.getLastSelectedRout() != -1) {
            for (i in routeList.indices) {
              if (routeList[i].iD == sharedPreference.getLastSelectedRout()) {
                rout_search_bar.selectItemByIndex(i)
              }
            }
          }
        })
  }

  private fun drawMapResrouce(route: Route) {
    if (::mMap.isInitialized) {
      mMap.clear()
      drawWayPointForGMap(route.iD.toString(), route.color)
      drawStopsForRout(route.iD.toString())

      vehicleTimerTask = object : TimerTask() {
        override fun run() {
          val handler = Handler(Looper.getMainLooper())
          handler.post {
            drawVehiclesFroRout(route.iD.toString())
            getCurrentLocation()
          }
        }
      }
    }
    Timer().scheduleAtFixedRate(
        vehicleTimerTask, 0, 5000
    )
  }

  private fun drawWayPointForGMap(
    id: String,
    color: String
  ) {
    realTimeMapViewModel.getWayPointLiveData(id)
        .observe(viewLifecycleOwner, Observer {
          val polyline1: Polyline = mMap.addPolyline(
              PolylineOptions()
                  .clickable(true)
                  .addAll(it)
                  .color(Color.parseColor(color))
          )
          mMap.animateCamera(
              CameraUpdateFactory.newLatLngZoom(
                  it[it.size / 2],
                  13f
              )
          )
        })
  }

  private fun drawStopsForRout(id: String) {
    realTimeMapViewModel.getRoutStops(id)
        .observe(viewLifecycleOwner, Observer {
          it.apply {
            forEach() { routStop ->
              Log.d(TAG, "draw stop for ${routStop.name}")
              mMap.addMarker(
                  MarkerOptions()
                      .position(LatLng(routStop.latitude, routStop.longitude))
                      .title(routStop.name)
                      .icon(
                          Util.bitmapDescriptorFromVector(context!!, R.drawable.ic_bus_stop, null)
                      )
              )

            }
          }
        })
  }

  private fun drawVehiclesFroRout(id: String) {
    realTimeMapViewModel.getRutVehicles(id)
        .observe(viewLifecycleOwner, Observer {

          it.apply {
            if (vehicleMarks.isEmpty()) {
              forEach { routVechicle ->
                val marker = MarkerOptions()
                    .position(LatLng(routVechicle.latitude, routVechicle.longitude))
                    .icon(
                        Util.bitmapDescriptorFromVector(
                            context!!, R.drawable.ic_navigation_black_24dp, null
                        )
                    )
                vehicleMarks.add(mMap.addMarker(marker))
              }
            } else {
              var counter = 0;
              forEach { routVechicle ->
                if (counter < vehicleMarks.size) {
                  val oldMarker = vehicleMarks[counter]
                  val oldPos = oldMarker.position
                  val newPos = LatLng(routVechicle.latitude, routVechicle.longitude)
                  oldMarker.position = newPos
//                Util.changePositionSmoothly(oldMarker, newPos)
                  mMap.animateCamera(
                      CameraUpdateFactory.newLatLngZoom(
                          newPos,
                          16f
                      )
                  )
                  rotateMarker(
                      oldMarker, Util.bearingBetweenLocations(oldPos, newPos)
                      .toFloat()
                  )
                  counter++
                }
              }
            }
          }
        })
  }

  private fun getCurrentLocation() {
    val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (ActivityCompat.checkSelfPermission(
            context!!, permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context!!, permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
      return
    }

    val locationListener = object : LocationListener {
      override fun onLocationChanged(p0: Location?) {
        if (p0 == null) return
        mMap.addMarker(
            MarkerOptions().position(LatLng(p0.latitude, p0.longitude))
                .icon(
                    Util.bitmapDescriptorFromVector(
                        context!!, R.drawable.ic_radio_button_checked_black_24dp, null
                    )
                )
                .title("My Location")
        )

        myLocation = LatLng(p0.latitude, p0.longitude)
      }

      override fun onStatusChanged(
        p0: String?,
        p1: Int,
        p2: Bundle?
      ) {
      }

      override fun onProviderEnabled(p0: String?) {
      }

      override fun onProviderDisabled(p0: String?) {
      }

    }

    locationManager.requestLocationUpdates(
        LocationManager.NETWORK_PROVIDER, 5000, 10f, locationListener
    )
    locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER, 5000, 10f, locationListener
    )
  }

  override fun onStop() {
    super.onStop()
    if (::vehicleTimerTask.isInitialized) {
      vehicleTimerTask.cancel()
    }
  }

  override fun onMapReady(p0: GoogleMap?) {
    if (p0 != null) {
      mMap = p0
      mMap.apply {
        mapType = GoogleMap.MAP_TYPE_NORMAL
      }
    }
  }

  private fun rotateMarker(
    marker: Marker,
    toRotation: Float
  ) {
    if (!isMarkerRotating) {
      val handler = Handler()
      val start = SystemClock.uptimeMillis()
      val startRotation = marker.rotation
      val duration: Long = 1000
      val interpolator: Interpolator = LinearInterpolator()
      handler.post(object : Runnable {
        override fun run() {
          isMarkerRotating = true
          val elapsed = SystemClock.uptimeMillis() - start
          val t: Float = interpolator.getInterpolation(elapsed.toFloat() / duration)
          val rot = t * toRotation + (1 - t) * startRotation
          marker.rotation = if (-rot > 180) rot / 2 else rot
          if (t < 1.0) {
            // Post again 16ms later.
            handler.postDelayed(this, 16)
          } else {
            isMarkerRotating = false
          }
        }
      })
    }
  }

  companion object {
    @JvmStatic
    private val TAG = RealTimeMapFragment::class.java.simpleName
  }
}
