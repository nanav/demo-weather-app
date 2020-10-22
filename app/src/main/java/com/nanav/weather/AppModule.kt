package com.nanav.weather

import com.nanav.weather.ui.landing.LandingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val ViewModelModule = module {

    viewModel {
        LandingViewModel(
            dataManager = get()
        )
    }

}

