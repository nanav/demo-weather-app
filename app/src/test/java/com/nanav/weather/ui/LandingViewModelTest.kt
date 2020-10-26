package com.nanav.weather.ui


import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nanav.weather.BuildConfig
import com.nanav.weather.ui.landing.LandingFlowState
import com.nanav.weather.ui.landing.LandingViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(sdk = [BuildConfig.TEST_VERSION])
class LandingViewModelTest {

    private lateinit var landingViewModel: LandingViewModel

    @Mock
    lateinit var flowObserver: Observer<LandingFlowState>

    private val SEARCH_INPUT_1 = "madrid"
    private val SEARCH_INPUT_2 = "ma"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        landingViewModel = LandingViewModel()
    }

    @Test
    fun `test 1 Data Load`() {
        //GIVEN
        val argumentCaptor = ArgumentCaptor.forClass(LandingFlowState::class.java)
        landingViewModel.landingFlowState.observeForever(flowObserver)

        //WHEN
        landingViewModel.search(SEARCH_INPUT_1)

        //THEN

        argumentCaptor.run {
            verify(flowObserver, times(2)).onChanged(argumentCaptor.capture())
        }

        val values = argumentCaptor.allValues

        assertEquals(2, values.size)
        assertEquals(LandingFlowState.LandingFlowLoading::class.java, values[0]::class.java)
        assertEquals(LandingFlowState.LandingFlowStartSearch::class.java, values[1]::class.java)
        assertEquals(
            SEARCH_INPUT_1,
            (values[1] as LandingFlowState.LandingFlowStartSearch).search
        )
    }

    @Test
    fun `test 2 Error Load`() {
        //GIVEN
        val argumentCaptor = ArgumentCaptor.forClass(LandingFlowState::class.java)
        landingViewModel.landingFlowState.observeForever(flowObserver)

        //WHEN
        landingViewModel.search(SEARCH_INPUT_2)

        //THEN

        argumentCaptor.run {
            verify(flowObserver, times(2)).onChanged(argumentCaptor.capture())
        }

        val values = argumentCaptor.allValues

        assertEquals(2, values.size)
        assertEquals(LandingFlowState.LandingFlowLoading::class.java, values[0]::class.java)
        assertEquals(LandingFlowState.LandingFlowError::class.java, values[1]::class.java)
    }
}
