package com.nanav.weather.ui.landing

import androidx.lifecycle.MutableLiveData
import com.nanav.weather.arch.BaseViewModel
import com.nanav.weather.data.managers.contract.DataManager
import com.nanav.weather.ext.rx.applyIoSchedulers
import com.nanav.weather.ui.landing.LandingDataState.*
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class LandingViewModel constructor(private val dataManager: DataManager) : BaseViewModel(),
    LandingMvvm.ViewModel {

    val landingDataState = MutableLiveData<LandingDataState>()

    override fun search(inputCity: String) {
        landingDataState.value = LandingDataLoading

        dataManager.getWeather(inputCity)
            .applyIoSchedulers()
            .subscribe({
                landingDataState.value = LandingData(it)
            }, {
                Timber.e(it)
                landingDataState.value = LandingDataError(
                    it.message
                        ?: it.javaClass.simpleName
                )
            }).addTo(subscription)

    }
}
