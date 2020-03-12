package com.studio.eddy.myapplication.ui.home

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eddystudio.shuttletracker.R
import com.eddystudio.shuttletracker.data.MySharedPreference
import com.eddystudio.shuttletracker.data.model.Route
import com.eddystudio.shuttletracker.ui.realtimemap.TrackerAdapter
import com.eddystudio.shuttletracker.util.Util
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.SphericalUtil
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.studio.eddy.myapplication.dagger.MyApplication
import kotlinx.android.synthetic.main.fragment_real_time_map.get_my_location_bt
import kotlinx.android.synthetic.main.fragment_real_time_map.real_time_coordinator_layout
import kotlinx.android.synthetic.main.fragment_real_time_map.real_time_shuttle_focus_fab
import kotlinx.android.synthetic.main.fragment_real_time_map.rout_search_bar
import kotlinx.android.synthetic.main.realtime_bottom_sheet_view.bottom_sheet
import kotlinx.android.synthetic.main.realtime_bottom_sheet_view.bottom_sheet_appbarlayout
import kotlinx.android.synthetic.main.realtime_bottom_sheet_view.bottom_sheet_collapsing_toolbar_layout
import kotlinx.android.synthetic.main.realtime_bottom_sheet_view.bottom_sheet_fab
import kotlinx.android.synthetic.main.realtime_bottom_sheet_view.bottom_sheet_recycler_view
import kotlinx.android.synthetic.main.realtime_bottom_sheet_view.bottom_sheet_toolbar
import java.util.Collections
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class RealTimeMapFragment : Fragment(), OnMapReadyCallback, OnCameraMoveStartedListener {

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
  private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
  private lateinit var selectedRoute: Route
  private var isDarkModeEnabled: Boolean = false
  private var isCameraFollowShuttle = false
  private var cameraFollowShuttleIndex = 0

  private val trackerAdapter: TrackerAdapter by lazy { TrackerAdapter(ArrayList()) }

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
    initBottomSheetView()
    initFab()
    handleOnBackPressed()
    rout_search_bar.apply {
      this.lifecycleOwner = viewLifecycleOwner
      this.setOnSpinnerItemSelectedListener(object : OnSpinnerItemSelectedListener<String> {
        override fun onItemSelected(
          position: Int,
          item: String
        ) {
          Log.d(TAG, "$item selected, pos = $position")
          selectedRoute = routeList[position]
          clearAllVehicleMarkers()
          if (::vehicleTimerTask.isInitialized) {
            vehicleTimerTask.cancel()
          }
          drawMapResrouce(selectedRoute)
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

  private fun handleOnBackPressed() {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner, object : OnBackPressedCallback(
        bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED
    ) {
      override fun handleOnBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
          bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
          if (!findNavController().navigateUp()) {
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
              activity?.finishAndRemoveTask()
            } else {
              activity?.finish()
            }
          }
        }
      }
    })
  }

  private fun scaleOffset(offset: Float): Float {
    val newOffSet = 1f - offset * 4.25f
    if (newOffSet <= 0) {
      return 0f
    }
    return newOffSet
  }

  private fun initBottomSheetView() {
    bottom_sheet_recycler_view.apply {
      layoutManager = LinearLayoutManager(context)
      this.adapter = trackerAdapter
    }
    bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

    bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onSlide(
        bottomSheet: View,
        slideOffset: Float
      ) {

        real_time_shuttle_focus_fab.scaleX = scaleOffset(slideOffset)
        real_time_shuttle_focus_fab.scaleY = scaleOffset(slideOffset)

        if (::selectedRoute.isInitialized && slideOffset > 0.5) {
          bottom_sheet_collapsing_toolbar_layout.title = selectedRoute.name
        } else {
          bottom_sheet_collapsing_toolbar_layout.title = getString(R.string.bottom_sheet_title)
        }
      }

      override fun onStateChanged(
        bottomSheet: View,
        newState: Int
      ) {
        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
          bottom_sheet_appbarlayout.setExpanded(false)
        } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
          bottom_sheet_appbarlayout.setExpanded(true)
        }
      }

    })

    bottom_sheet_toolbar.setOnClickListener {
      if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

      } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
      }
    }


    bottom_sheet_fab.setOnClickListener {
      bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
  }

  private fun initFab() {
    real_time_shuttle_focus_fab.setOnClickListener {
      if (::mMap.isInitialized && vehicleMarks.size > 0) {
        isCameraFollowShuttle = true
        if (cameraFollowShuttleIndex + 1 >= vehicleMarks.size) {
          cameraFollowShuttleIndex = 0
        } else {
          cameraFollowShuttleIndex++
        }
        moveCameraTo(vehicleMarks[cameraFollowShuttleIndex].position, 16f)
        showSnackBar("Shuttle: ${vehicleMarks[cameraFollowShuttleIndex].title}")
      } else {
        showSnackBar("There is no running shuttle at this moment!")
      }
    }
  }

  private fun showSnackBar(message: String) {
    Snackbar.make(
            real_time_coordinator_layout,
            message,
            BaseTransientBottomBar.LENGTH_LONG
        )
        .setAnchorView(real_time_shuttle_focus_fab)
        .show()
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
            isCameraFollowShuttle = false
            drawVehiclesFroRout(route.iD.toString())
            getCurrentLocation()
          }
        }
      }
    }
    Timer().scheduleAtFixedRate(
        vehicleTimerTask, 0, VEHICLE_UPDATE_FREQUENT
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
                  .color(
                      ContextCompat.getColor(
                          context!!, Util.materialColorMapper(id, isDarkModeEnabled)
                      )
                  )
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
          trackerAdapter.addStopsData(it)
          it.apply {
            forEach() { routStop ->
              Log.d(TAG, "draw stop for ${routStop.name}")
              mMap.addMarker(
                  MarkerOptions()
                      .position(LatLng(routStop.latitude, routStop.longitude))
                      .title(routStop.name)
                      .icon(
                          Util.bitmapDescriptorFromVector(
                              context!!, R.drawable.ic_bus_stop_sign_3, null, 1.3f
                          )
                      )
              )

            }
          }
        })
  }

  private fun drawVehiclesFroRout(id: String) {
    realTimeMapViewModel.getRutVehicles(id)
        .observe(viewLifecycleOwner, Observer {
//          trackerAdapter.removeAllRoutStop()
//          trackerAdapter.addDataToPos(it,0)
          trackerAdapter.addVehicleData(it)
          it.apply {
            if (vehicleMarks.isEmpty()) {
              forEach { routVehicle ->
                val marker = MarkerOptions()
                    .position(LatLng(routVehicle.latitude, routVehicle.longitude))
                    .icon(
                        Util.bitmapDescriptorFromVector(
                            context!!, R.drawable.ic_shuttle_6, null, 1.4f
                        )
                    )
                    .anchor(0.5f, 0.5f)
                    .title(this[0].name)
                vehicleMarks.add(mMap.addMarker(marker))
              }
            } else {
              forEachIndexed { index, routVehicle ->
                if (index < vehicleMarks.size) {
                  val oldMarker = vehicleMarks[index]
                  val oldPos = oldMarker.position
                  val newPos = LatLng(routVehicle.latitude, routVehicle.longitude)
                  Util.changePositionSmoothly(oldMarker, newPos)
                  rotateMarker(
                      oldMarker,
                      SphericalUtil.computeHeading(oldPos, newPos)
                          .toFloat()
                  )
                }
              }
              if (isCameraFollowShuttle && cameraFollowShuttleIndex < vehicleMarks.size) {
                moveCameraTo(vehicleMarks[cameraFollowShuttleIndex].position, 16f)
              }
            }
          }
        })
  }

  private fun moveCameraTo(
    pos: LatLng,
    scale: Float
  ) {
    mMap.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            pos,
            scale
        )
    )
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
        if (p0 == null || context == null) return

        mMap.addMarker(
            MarkerOptions().position(LatLng(p0.latitude, p0.longitude))
                .icon(
                    Util.bitmapDescriptorFromVector(
                        context!!, R.drawable.ic_radio_button_checked_black_24dp, null, 1f
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
      mMap.let {
        it.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (isDarkModeEnabled) {
          it.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_night_style)
          )
        }
        it.setOnCameraMoveStartedListener(this)
      }

    }
  }

  override fun onCameraMoveStarted(p0: Int) {
    if (p0 == OnCameraMoveStartedListener.REASON_GESTURE) {
      isCameraFollowShuttle = false
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
    const val VEHICLE_UPDATE_FREQUENT = 5000L
  }

}
