package com.nanav.weather.ui.landing

import com.nanav.weather.arch.BaseMvvm
import com.nanav.weather.data.model.Weather

interface LandingMvvm {

    interface ViewModel : BaseMvvm.ViewModel {
        fun search(inputCity: String)
    }

}

sealed class LandingDataState {
    object LandingDataLoading : LandingDataState()
    class LandingDataError(val errorMessage: String) : LandingDataState()
    class LandingData(val weather: Weather) : LandingDataState()
}
