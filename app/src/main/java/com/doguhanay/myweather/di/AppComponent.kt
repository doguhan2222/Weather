package com.doguhanay.myweather.di

import com.doguhanay.myweather.ui.HomeScreen
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(fragment:HomeScreen)
}