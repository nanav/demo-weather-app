package com.nanav.weather.data.api

import com.nanav.weather.data.BuildConfig
import com.nanav.weather.data.model.Weather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/${BuildConfig.API_VERSION}/weather?q=madrid&appid=${BuildConfig.API_KEY}")
    fun getWeather(@Query("q") inputCity:String): Single<Weather>

}
