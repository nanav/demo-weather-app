package com.nanav.weather.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nanav.weather.BuildConfig.TEST_VERSION
import com.nanav.weather.data.api.WeatherService
import com.nanav.weather.data.managers.DataManagerImpl
import com.nanav.weather.data.managers.contract.DataManager
import com.nanav.weather.data.model.MockWeather
import com.nanav.weather.data.model.Weather
import com.nanav.weather.util.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@Config(sdk = [TEST_VERSION])
class DataManagerTest {

    @Rule
    @JvmField
    var scheduleOverride = RxSchedulersOverrideRule()

    private lateinit var dataManager: DataManager

    @Mock
    lateinit var weatherService: WeatherService

    private val SEARCH_INPUT_1 = "madrid"

    @Before
    @Throws
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(weatherService.getWeather(SEARCH_INPUT_1))
            .thenReturn(Single.just(MockWeather.WEATHER_MAD))

        dataManager = DataManagerImpl(weatherService)
    }

    @Test
    fun `test Get Character Success`() {
        val testObserver = TestObserver<Weather>()

        dataManager.getWeather(SEARCH_INPUT_1).subscribe(testObserver)

        testObserver.awaitTerminalEvent(50, TimeUnit.MILLISECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]

        assertEquals(result.city, MockWeather.WEATHER_MAD.city)
        assertEquals(result.id, MockWeather.WEATHER_MAD.id)
    }
}
