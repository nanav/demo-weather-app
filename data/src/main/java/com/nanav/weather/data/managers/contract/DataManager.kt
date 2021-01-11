package com.nanav.weather.data.managers.contract

import com.nanav.weather.data.model.Weather

interface DataManager {
    suspend fun getWeather(inputCity: String): Weather
}
