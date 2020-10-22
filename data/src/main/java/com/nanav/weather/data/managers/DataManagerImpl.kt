package com.nanav.weather.data.managers

import com.nanav.weather.data.api.WeatherService
import com.nanav.weather.data.managers.contract.DataManager
import com.nanav.weather.data.model.Weather
import io.reactivex.Single

class DataManagerImpl constructor(
    private val weatherService: WeatherService
) : DataManager {

    override fun getWeather(inputCity: String): Single<Weather> {
        return weatherService.getWeather(inputCity)
    }

}
