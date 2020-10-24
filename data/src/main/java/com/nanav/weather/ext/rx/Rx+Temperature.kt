package com.nanav.weather.ext.rx

fun Int.toKelvin(): Float = this.toFloat() + 273.15f

fun Float.toCelsius(): Float = this - 273.15f