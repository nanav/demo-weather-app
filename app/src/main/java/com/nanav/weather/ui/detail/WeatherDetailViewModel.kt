package com.nanav.weather.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.nanav.weather.R
import com.nanav.weather.arch.BaseViewModel
import com.nanav.weather.data.managers.contract.DataManager
import com.nanav.weather.data.model.Weather
import com.nanav.weather.ui.detail.WeatherDataState.WeatherDataLoading
import timber.log.Timber

class WeatherDetailViewModel constructor(private val dataManager: DataManager) : BaseViewModel(),
    WeatherDetailMvvm.ViewModel {

    var weatherDetailDataState: LiveData<out WeatherDataState>
    private val citySearch = MutableLiveData<String>()

    private var weather: Weather? = null

    init {
        weatherDetailDataState = citySearch.switchMap {
            liveData {

                try {
                    emit(WeatherDataLoading)
                    weather = dataManager.getWeather(it)
                    emit(WeatherDataState.WeatherData(weather!!))

                } catch (throwable: Throwable) {
                    Timber.e(throwable)
                    emit(
                        WeatherDataState.WeatherDataError(
                            if (throwable.message?.contains("404") == true) {
                                R.string.weather_search_not_found
                            } else {
                                R.string.generic_error
                            }
                        )
                    )
                }
            }
        }
    }

    override fun search(inputCity: String) {

        citySearch.value = inputCity

        // TODO ALT METHOD: viewmodelscope
        //onViewModelScopeSet(inputCity)

    }


    private fun onViewModelScopeSet(inputCity: String) {
        /*
        weatherDetailDataState.value = WeatherDataLoading
        viewModelScope.launch {
            val result = try {
            val res = dataManager.getWeather(inputCity)
            weatherDetailDataState.value = WeatherData(res)

        } catch (e: Exception) {
            //Result.Error(Exception("Cannot open HttpURLConnection"))
            Timber.e(e)
            weatherDetailDataState.value = WeatherDataError(
                if (e.message?.contains("404") == true) {
                    R.string.weather_search_not_found
                } else {
                    R.string.generic_error
                }
            )
        }*/
    }
}
