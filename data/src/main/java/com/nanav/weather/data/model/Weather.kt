package com.nanav.weather.data.model

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName(value = "name")
    val city: String,
    val id: Int
)
