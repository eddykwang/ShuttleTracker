package com.studio.eddy.myapplication.dagger

import android.app.Application
import com.eddystudio.shuttletracker.dagger.ContextModule

class MyApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    component = DaggerApplicationComponent.builder()
        .contextModule(ContextModule(this))
        .build()
    component.inject(this)
  }

  companion object {
    lateinit var component: ApplicationComponent
    public fun getAppComponet(): ApplicationComponent {
      return component
    }
  }

}