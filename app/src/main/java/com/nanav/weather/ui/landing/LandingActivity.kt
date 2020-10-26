package com.nanav.weather.ui.landing

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.nanav.weather.arch.BaseMvvmActivity
import com.nanav.weather.databinding.ActivityLandingBinding
import com.nanav.weather.ext.android.hideKeyboard
import com.nanav.weather.ui.detail.WeatherDetailActivity

class LandingActivity : BaseMvvmActivity<LandingViewModel, ActivityLandingBinding>(
    LandingViewModel::class,
    ActivityLandingBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.landingFlowState.observe { processDataState(it) }

        RxTextView.editorActions(layout.landingInput)
            .subscribe {
                if (it == EditorInfo.IME_ACTION_DONE) {
                    onSearchAction()
                }
            }.disposeOnDestroy()

        RxView.clicks(layout.landingSearchBtn).subscribe { onSearchAction() }.disposeOnDestroy()
    }

    private fun onSearchAction() {
        hideKeyboard()
        viewModel.search(layout.landingInput.text.toString())
    }

    override fun onResume() {
        super.onResume()
        showProgress(false)
    }

    private fun processDataState(landingFlowState: LandingFlowState) {
        when (landingFlowState) {
            LandingFlowState.LandingFlowLoading -> showProgress(true)
            is LandingFlowState.LandingFlowError -> showError(landingFlowState.errorMessage)
            is LandingFlowState.LandingFlowStartSearch -> startSearch(landingFlowState.search)
        }
    }

    private fun showProgress(isLoading: Boolean) {
        layout.landingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(@StringRes errorMsg: Int) {
        showProgress(false)
        layout.landingInput.error = getString(errorMsg)
    }

    private fun startSearch(search: String) {
        WeatherDetailActivity.start(this, search)
    }
}
