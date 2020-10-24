package com.nanav.weather.ui.landing

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxbinding2.widget.RxTextView
import com.nanav.weather.R
import com.nanav.weather.arch.BaseMvvmActivity
import com.nanav.weather.data.model.Weather
import com.nanav.weather.databinding.ActivityLandingBinding
import com.nanav.weather.ext.android.hideKeyboard
import com.nanav.weather.ext.rx.toCelsius

class LandingActivity : BaseMvvmActivity<LandingViewModel, ActivityLandingBinding>(
    LandingViewModel::class,
    ActivityLandingBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.landingDataState.observe { processDataState(it) }

        RxTextView.editorActions(layout.landingInput)
            .subscribe {
                if (it == EditorInfo.IME_ACTION_DONE) {
                    viewModel.search(layout.landingInput.text.toString())
                }
            }.disposeOnDestroy()
    }

    private fun processDataState(landingDataState: LandingDataState) {
        when (landingDataState) {
            LandingDataState.LandingDataLoading -> showProgress(true)
            is LandingDataState.LandingDataError -> showMessage(landingDataState.errorMessage)
            is LandingDataState.LandingData -> setData(landingDataState.weather)
        }
    }

    private fun setData(weather: Weather) {
        hideKeyboard()

        showProgress(false)

        layout.landingCity.text = weather.city
        layout.landingHumidity.text = String.format(
            getString(R.string.landing_hum_value),
            weather.weatherMain.humidity.toInt()
        )
        layout.landingTemp.text = String.format(
            getString(R.string.landing_temp_value),
            weather.weatherMain.temp.toCelsius(),
            weather.weatherMain.feelsLike.toCelsius()
        )
    }

    private fun showProgress(isLoading: Boolean) {
        layout.landingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        layout.landingInfo.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        layout.landingError.visibility = View.GONE
    }

    private fun showMessage(message: String) {
        layout.landingInfo.visibility = View.GONE
        layout.landingError.visibility = View.VISIBLE
        layout.landingError.text = message
    }
}
