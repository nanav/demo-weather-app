package com.nanav.weather.data.managers

import com.nanav.weather.data.api.WeatherService
import com.nanav.weather.data.managers.contract.DataManager
import com.nanav.weather.data.model.Weather

class DataManagerImpl constructor(private val weatherService: WeatherService) : DataManager {

    override suspend fun getWeather(inputCity: String): Weather =
        weatherService.getWeather(inputCity)
}
