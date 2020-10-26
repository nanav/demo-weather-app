package com.nanav.weather.ui.detail

import androidx.lifecycle.MutableLiveData
import com.nanav.weather.R
import com.nanav.weather.arch.BaseViewModel
import com.nanav.weather.data.managers.contract.DataManager
import com.nanav.weather.ext.rx.applyIoSchedulers
import com.nanav.weather.ui.detail.WeatherDataState.*
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class WeatherDetailViewModel constructor(private val dataManager: DataManager) : BaseViewModel(),
    WeatherDetailMvvm.ViewModel {

    val weatherDetailDataState = MutableLiveData<WeatherDataState>()

    override fun search(inputCity: String) {
        weatherDetailDataState.value = WeatherDataLoading

        dataManager.getWeather(inputCity)
            .applyIoSchedulers()
            .subscribe({
                weatherDetailDataState.value = WeatherData(it)
            }, {
                Timber.e(it)
                weatherDetailDataState.value = WeatherDataError(
                    if (it.message?.contains("404") == true) {
                        R.string.weather_search_not_found
                    } else {
                        R.string.generic_error
                    }
                )
            }).addTo(subscription)

    }
}
