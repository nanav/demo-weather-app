package com.nanav.weather.data.model

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName(value = "name")
    val city: String,
    val id: Int,
    @SerializedName(value = "main")
    val weatherMain: WeatherMain,
    @SerializedName(value = "weather")
    val weatherItems: List<WeatherItem>
)

data class WeatherMain(
    val temp: Float,
    @SerializedName(value = "feels_like")
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
    val pressure: Float,
    val humidity: Float
)

data class WeatherItem(
    val main: String,
    val description: String
)