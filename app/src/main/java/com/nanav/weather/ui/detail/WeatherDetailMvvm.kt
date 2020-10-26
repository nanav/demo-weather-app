package com.nanav.weather.ui.detail

import androidx.annotation.StringRes
import com.nanav.weather.arch.BaseMvvm
import com.nanav.weather.data.model.Weather

interface WeatherDetailMvvm {

    interface ViewModel : BaseMvvm.ViewModel {
        fun search(inputCity: String)
    }

}

sealed class WeatherDataState {
    object WeatherDataLoading : WeatherDataState()
    class WeatherDataError(@StringRes val errorMessage: Int) : WeatherDataState()
    class WeatherData(val weather: Weather) : WeatherDataState()
}
