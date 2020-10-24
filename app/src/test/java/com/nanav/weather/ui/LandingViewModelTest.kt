package com.nanav.weather.ui


import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nanav.weather.BuildConfig
import com.nanav.weather.data.managers.contract.DataManager
import com.nanav.weather.data.model.MockWeather
import com.nanav.weather.ui.landing.LandingDataState
import com.nanav.weather.ui.landing.LandingViewModel
import com.nanav.weather.util.RxSchedulersOverrideRule
import io.reactivex.Single
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(sdk = [BuildConfig.TEST_VERSION])
class LandingViewModelTest {
    @Rule
    @JvmField
    var scheduleOverride = RxSchedulersOverrideRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var landingViewModel: LandingViewModel

    private val throwable: Throwable = Throwable("Something went wrong loading data")

    @Mock
    lateinit var dataObserver: Observer<LandingDataState>

    private val SEARCH_INPUT_1 = "madrid"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        `when`(dataManager.getWeather(SEARCH_INPUT_1))
            .thenReturn(Single.just(MockWeather.WEATHER_MAD))

        landingViewModel = LandingViewModel(dataManager)
    }

    @Test
    fun `test 1 Data Load`() {
        //GIVEN
        val argumentCaptor = ArgumentCaptor.forClass(LandingDataState::class.java)
        landingViewModel.landingDataState.observeForever(dataObserver)

        //WHEN
        `when`(dataManager.getWeather(SEARCH_INPUT_1)).thenReturn(Single.just(MockWeather.WEATHER_MAD))

        landingViewModel.search(SEARCH_INPUT_1)

        //THEN

        argumentCaptor.run {
            verify(dataObserver, times(2)).onChanged(argumentCaptor.capture())
        }

        val values = argumentCaptor.allValues

        assertEquals(2, values.size)
        assertEquals(LandingDataState.LandingDataLoading::class.java, values[0]::class.java)
        assertEquals(LandingDataState.LandingData::class.java, values[1]::class.java)
        assertEquals(
            MockWeather.WEATHER_MAD,
            (values[1] as LandingDataState.LandingData).weather
        )
    }

    @Test
    fun `test 2 Error Load`() {
        //GIVEN
        val argumentCaptor = ArgumentCaptor.forClass(LandingDataState::class.java)
        landingViewModel.landingDataState.observeForever(dataObserver)

        //WHEN
        `when`(dataManager.getWeather(SEARCH_INPUT_1)).thenReturn(Single.error(throwable))

        landingViewModel.search(SEARCH_INPUT_1)

        //THEN

        argumentCaptor.run {
            verify(dataObserver, times(2)).onChanged(argumentCaptor.capture())
        }

        val values = argumentCaptor.allValues

        assertEquals(2, values.size)
        assertEquals(LandingDataState.LandingDataLoading::class.java, values[0]::class.java)
        assertEquals(LandingDataState.LandingDataError::class.java, values[1]::class.java)
    }
}
