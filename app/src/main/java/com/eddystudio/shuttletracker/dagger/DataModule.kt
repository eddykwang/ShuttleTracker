package com.eddystudio.shuttletracker.dagger

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class DataModule {

  @Provides
  fun provideSharedPreference(context: Context): SharedPreferences {
    return context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
  }
}