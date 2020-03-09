package com.eddystudio.shuttletracker

import android.Manifest.permission
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.studio.eddy.myapplication.dagger.MyApplication
import java.util.Objects

class MainActivity : AppCompatActivity() {
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    MyApplication.component.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val navView: BottomNavigationView = findViewById(R.id.nav_view)

    navController = findNavController(R.id.nav_host_fragment)

    navView.setupWithNavController(navController)

    setUpGetCurrentLocation()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
      LOCATION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
      ) {
        setUpGetCurrentLocation()
      } else {
        val alertDialog =
          Builder(
              Objects.requireNonNull(this)
          )
              .create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("Failed")
        alertDialog.setMessage("You need to grant Location permission to use the app.")
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Exit App"
        ) { dialog: DialogInterface, which: Int -> this.finish() }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "Request"
        ) { dialog: DialogInterface?, which: Int -> setUpGetCurrentLocation() }
        alertDialog.show()
      }
    }
  }

  fun setUpGetCurrentLocation() {

    if (ActivityCompat.checkSelfPermission(
            this, permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            this, permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        requestPermissions(
            arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_REQUEST_CODE
        )
      }
    } else {
    }
  }

  companion object {
    const val LOCATION_REQUEST_CODE = 4344

  }

}
