package com.nanav.weather.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nanav.weather.BuildConfig.TEST_VERSION
import com.nanav.weather.data.api.WeatherService
import com.nanav.weather.data.managers.DataManagerImpl
import com.nanav.weather.data.managers.contract.DataManager
import com.nanav.weather.data.model.MockWeather
import com.nanav.weather.ui.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [TEST_VERSION])
class DataManagerTest {

    @get: Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var dataManager: DataManager

    @Mock
    lateinit var weatherService: WeatherService

    private val SEARCH_INPUT_1 = "madrid"

    @Before
    @Throws
    fun setup() {
        MockitoAnnotations.initMocks(this)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            Mockito.`when`(weatherService.getWeather(SEARCH_INPUT_1))
                .thenReturn(MockWeather.WEATHER_MAD)
        }

        dataManager = DataManagerImpl(weatherService)
    }

    @Test
    fun `test Get Data Success`() = coroutinesTestRule.testDispatcher.runBlockingTest {

        val result = dataManager.getWeather(SEARCH_INPUT_1)

        assertEquals(result.city, MockWeather.WEATHER_MAD.city)
        assertEquals(result.id, MockWeather.WEATHER_MAD.id)
    }
}
