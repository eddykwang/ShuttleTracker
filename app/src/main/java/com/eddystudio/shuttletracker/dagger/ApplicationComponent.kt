package com.studio.eddy.myapplication.dagger

import com.eddystudio.shuttletracker.MainActivity
import com.eddystudio.shuttletracker.dagger.ContextModule
import com.eddystudio.shuttletracker.dagger.DataModule
import com.studio.eddy.myapplication.ui.home.RealTimeMapFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, DataModule::class, ContextModule::class])
interface ApplicationComponent {
  fun inject(app: MyApplication)
  fun inject(realTimeMapFragment: RealTimeMapFragment)
  fun inject(mainActivity: MainActivity)
}