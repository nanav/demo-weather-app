package com.nanav.weather.ui.landing

import androidx.lifecycle.MutableLiveData
import com.nanav.weather.R
import com.nanav.weather.arch.BaseViewModel
import com.nanav.weather.ui.landing.LandingFlowState.*

class LandingViewModel : BaseViewModel(), LandingMvvm.ViewModel {

    val landingFlowState = MutableLiveData<LandingFlowState>()

    override fun search(inputCity: String) {
        landingFlowState.value = LandingFlowLoading

        if (inputCity.length > 3) {
            landingFlowState.value = LandingFlowStartSearch(inputCity)
        } else {
            landingFlowState.value = LandingFlowError(R.string.landing_search_error)
        }
    }
}
