package com.nanav.weather

import android.app.Application
import android.os.Looper
import com.nanav.weather.arch.ApiModule
import com.nanav.weather.arch.DataModule
import com.nanav.weather.ext.TimberKoinLogger
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

open class WeatherApp : Application() {

    override fun onCreate() {

        super.onCreate()

        startKoin {
            logger(TimberKoinLogger())
            androidContext(this@WeatherApp)
        }

        loadKoinModules(
            listOf(
                ViewModelModule,
                ApiModule,
                DataModule
            )
        )

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        JodaTimeAndroid.init(this)

        //Reportedly improves the speed of RX by allowing RX to be executed outside of Android 16ms Main thread frame
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            AndroidSchedulers.from(
                Looper.getMainLooper(),
                true
            )
        }
    }

}
