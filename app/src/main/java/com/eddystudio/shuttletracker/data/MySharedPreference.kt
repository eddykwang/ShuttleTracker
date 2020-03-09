package com.eddystudio.shuttletracker.data

import android.content.SharedPreferences
import javax.inject.Inject

class MySharedPreference @Inject constructor(private val sharedPreference: SharedPreferences) {

  fun storeLastSelectedRout(id: Int) {
    sharedPreference.edit()
        .apply {
          putInt(LAST_SELECTED_ROUT_PRAM, id)
          apply()
        }
  }

  fun getLastSelectedRout(): Int {
    return sharedPreference.getInt(LAST_SELECTED_ROUT_PRAM, -1)
  }

  companion object {
    @JvmStatic
    private val LAST_SELECTED_ROUT_PRAM = "last_selected_rout"

  }
}