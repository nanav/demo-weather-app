package com.nanav.weather.data.api

import com.nanav.weather.data.BuildConfig
import com.nanav.weather.data.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/${BuildConfig.API_VERSION}/weather?appid=${BuildConfig.API_KEY}")
    suspend fun getWeather(@Query("q") inputCity: String): Weather

}
