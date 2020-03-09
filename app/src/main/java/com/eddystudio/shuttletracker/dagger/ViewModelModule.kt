package com.studio.eddy.myapplication.dagger

import com.studio.eddy.myapplication.ui.home.RealTimeMapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ClassKey(RealTimeMapViewModel::class)
    abstract fun bindHomeViewModel(realTimeMapViewModel: RealTimeMapViewModel) :RealTimeMapViewModel
}