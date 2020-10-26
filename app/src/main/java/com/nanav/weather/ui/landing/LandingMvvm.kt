package com.nanav.weather.ui.landing

import androidx.annotation.StringRes
import com.nanav.weather.arch.BaseMvvm

interface LandingMvvm {

    interface ViewModel : BaseMvvm.ViewModel {
        fun search(inputCity: String)
    }

}

sealed class LandingFlowState {
    object LandingFlowLoading : LandingFlowState()
    class LandingFlowError(@StringRes val errorMessage: Int) : LandingFlowState()
    class LandingFlowStartSearch(val search: String) : LandingFlowState()
}
