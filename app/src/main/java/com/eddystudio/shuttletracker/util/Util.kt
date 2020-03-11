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
import com.eddystudio.shuttletracker.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.SphericalUtil
import com.studio.eddy.myapplication.ui.home.RealTimeMapFragment

class Util {
  companion object {

    @JvmStatic
    public fun materialColorMapper(
      id: String,
      darkEnabled: Boolean
    ): Int {
      var newColor = R.color.ID_10306
      if (darkEnabled) {
        when (id) {
          "13273" -> newColor = R.color.ID_13273_dark
          "10306" -> newColor = R.color.ID_10306_dark
          "11496" -> newColor = R.color.ID_11496_dark
          "3442" -> newColor = R.color.ID_3442_dark
          "13933" -> newColor = R.color.ID_13933_dark
          "13948" -> newColor = R.color.ID_13948_dark
          "13946" -> newColor = R.color.ID_13946_dark
          "2399" -> newColor = R.color.ID_2399_dark
          "13638" -> newColor = R.color.ID_13638_dark
          "13668" -> newColor = R.color.ID_13668_dark
          "12565" -> newColor = R.color.ID_12565_dark
        }
      } else {
        when (id) {
          "13273" -> newColor = R.color.ID_13273
          "10306" -> newColor = R.color.ID_10306
          "11496" -> newColor = R.color.ID_11496
          "3442" -> newColor = R.color.ID_3442
          "13933" -> newColor = R.color.ID_13933
          "13948" -> newColor = R.color.ID_13948
          "13946" -> newColor = R.color.ID_13946
          "2399" -> newColor = R.color.ID_2399
          "13638" -> newColor = R.color.ID_13638
          "13668" -> newColor = R.color.ID_13668
          "12565" -> newColor = R.color.ID_12565
        }
      }

      return newColor
    }

    @JvmStatic
    public fun bitmapDescriptorFromVector(
      context: Context,
      vectorResId: Int,
      resId: Int?,
      scale: Float
    ): BitmapDescriptor? {
      val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
      if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && resId != null) {
        vectorDrawable!!.setTint(resId)
        vectorDrawable.setTintMode(MULTIPLY)
      }
      vectorDrawable!!.setBounds(
          0, 0, (vectorDrawable!!.intrinsicWidth / scale).toInt(),
          (vectorDrawable!!.intrinsicHeight / scale).toInt()
      )
      val bitmap = Bitmap.createBitmap(
          vectorDrawable!!.intrinsicWidth, vectorDrawable!!.intrinsicHeight,
          ARGB_8888
      )
      val canvas = Canvas(bitmap)
      vectorDrawable!!.draw(canvas)
      return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    public fun changePositionSmoothly(
      marker: Marker?,
      newLatLng: LatLng
    ) {
      if (marker == null) {
        return;
      }
      val animation = ValueAnimator.ofFloat(0f, 100f)
      var previousStep = 0f
      val deltaLatitude = newLatLng.latitude - marker.position.latitude
      val deltaLongitude = newLatLng.longitude - marker.position.longitude
      animation.setDuration(RealTimeMapFragment.VEHICLE_UPDATE_FREQUENT)
      animation.addUpdateListener { updatedAnimation ->
        val deltaStep = updatedAnimation.getAnimatedValue() as Float - previousStep
        previousStep = updatedAnimation.getAnimatedValue() as Float
        marker.position = LatLng(
            marker.position.latitude + deltaLatitude * deltaStep * 1 / 100,
            marker.position.longitude + deltaStep * deltaLongitude * 1 / 100
        )
      }
      animation.start()
    }
  }
}