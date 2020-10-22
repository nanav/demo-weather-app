package com.nanav.weather.data.managers.contract

import com.nanav.weather.data.model.Weather
import io.reactivex.Single

interface DataManager {
    fun getWeather(inputCity: String): Single<Weather>
}
