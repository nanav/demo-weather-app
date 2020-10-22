package com.nanav.weather.ui.landing

import android.os.Bundle
import android.view.View
import com.nanav.weather.arch.BaseMvvmActivity
import com.nanav.weather.data.model.Weather
import com.nanav.weather.databinding.ActivityPhotosBinding

class LandingActivity : BaseMvvmActivity<LandingViewModel, ActivityPhotosBinding>(
    LandingViewModel::class,
    ActivityPhotosBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.landingDataState.observe { processDataState(it) }

        viewModel.onCreate()
    }

    private fun processDataState(landingDataState: LandingDataState) {
        when (landingDataState) {
            LandingDataState.LandingDataLoading -> showProgress(true)
            is LandingDataState.LandingDataError -> showMessage(landingDataState.errorMessage)
            is LandingDataState.LandingData -> setData(landingDataState.weather)
        }
    }

    private fun setData(weather: Weather) {
        showProgress(false)
        layout.weatherCity.text = weather.city
        //todo
    }


    private fun showProgress(isLoading: Boolean) {
        layout.weatherProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        layout.weatherInfo.visibility = if (isLoading) View.GONE else View.VISIBLE
        layout.weatherError.visibility = View.GONE
    }

    private fun showMessage(message: String) {
        layout.weatherInfo.visibility = View.GONE
        layout.weatherError.visibility = View.VISIBLE
        layout.weatherError.text = message
    }
}
