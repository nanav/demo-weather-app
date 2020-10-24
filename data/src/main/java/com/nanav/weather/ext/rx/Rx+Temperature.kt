package com.nanav.weather.ext.rx

fun Int.toKelvin(): Float {
    return this.toFloat() + 273.15f
}

fun Float.toCelsius(): Float {
    return this - 273.15f
}