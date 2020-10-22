package com.nanav.weather

import android.app.Application
import com.nanav.weather.util.testing.TestTree
import timber.log.Timber

class TestWeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(TestTree())
    }

}
