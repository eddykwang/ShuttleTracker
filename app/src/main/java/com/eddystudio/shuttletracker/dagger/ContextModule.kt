package com.eddystudio.shuttletracker.dagger

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule constructor(private val context: Context) {

  @Provides
  fun provideContext(): Context {
    return context
  }

}