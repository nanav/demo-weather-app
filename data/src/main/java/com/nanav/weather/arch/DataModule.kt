package com.nanav.weather.arch

import com.nanav.weather.data.managers.*
import com.nanav.weather.data.managers.contract.DataManager
import org.koin.core.qualifier.named
import org.koin.dsl.module

val DataModule = module {

    single {
        RxConnectivity(
            context = get()
        )
    }

    single<DataManager> {
        DataManagerImpl(
            weatherService = get()
        )
    }

}
