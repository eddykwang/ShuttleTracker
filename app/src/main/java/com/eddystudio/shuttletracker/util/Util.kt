package com.eddystudio.shuttletracker.util

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.PorterDuff.Mode.MULTIPLY
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.SystemClock
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class Util {
  companion object {

    @JvmStatic
    public fun bitmapDescriptorFromVector(
      context: Context,
      vectorResId: Int,
      resId: Int?
    ): BitmapDescriptor? {
      val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
      if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && resId != null) {
        vectorDrawable!!.setTint(resId)
        vectorDrawable.setTintMode(MULTIPLY)
      }
      vectorDrawable!!.setBounds(
          0, 0, vectorDrawable!!.intrinsicWidth, vectorDrawable!!.intrinsicHeight
      )
      val bitmap = Bitmap.createBitmap(
          vectorDrawable!!.intrinsicWidth, vectorDrawable!!.intrinsicHeight,
          ARGB_8888
      )
      val canvas = Canvas(bitmap)
      vectorDrawable!!.draw(canvas)
      return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    public fun bearingBetweenLocations(
      latLng1: LatLng,
      latLng2: LatLng
    ): Double {
      val PI = 3.14159
      val lat1 = latLng1.latitude * PI / 180
      val long1 = latLng1.longitude * PI / 180
      val lat2 = latLng2.latitude * PI / 180
      val long2 = latLng2.longitude * PI / 180
      val dLon = long2 - long1
      val y = Math.sin(dLon) * Math.cos(lat2)
      val x =
        Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
            * Math.cos(lat2) * Math.cos(dLon))
      var brng = Math.atan2(y, x)
      brng = Math.toDegrees(brng)
      brng = (brng + 360) % 360
      return brng
    }

    public fun changePositionSmoothly(marker:Marker?, newLatLng: LatLng){
      if(marker == null){
        return;
      }
      val animation = ValueAnimator.ofFloat(0f, 100f)
      var previousStep = 0f
      val deltaLatitude = newLatLng.latitude - marker.position.latitude
      val deltaLongitude = newLatLng.longitude - marker.position.longitude
      animation.setDuration(1500)
      animation.addUpdateListener { updatedAnimation ->
        val deltaStep = updatedAnimation.getAnimatedValue() as Float - previousStep
        previousStep = updatedAnimation.getAnimatedValue() as Float
        marker.position = LatLng(marker.position.latitude + deltaLatitude * deltaStep * 1/100, marker.position.longitude + deltaStep * deltaLongitude * 1/100)
      }
      animation.start()
    }
  }
}