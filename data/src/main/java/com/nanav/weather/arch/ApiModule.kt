package com.nanav.weather.arch

import android.content.Context
import com.nanav.weather.data.BuildConfig
import com.nanav.weather.data.api.WeatherService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

private const val CACHE_SIZE = 10 * 1024 * 1024L //10MB

val ApiModule = module {
    //services

    single { get<Retrofit>().create(WeatherService::class.java) }

    //retrofit
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>(named("loggingInterceptor")))
            .cache(Cache(get<Context>().cacheDir, CACHE_SIZE))
            .build()
    }

    //Okhttp Interceptors
    single(named("loggingInterceptor")) {
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.d(message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}
