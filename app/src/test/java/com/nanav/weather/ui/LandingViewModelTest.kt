package com.nanav.weather.ui


import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nanav.marveltest.BuildConfig
import com.nanav.marveltest.data.managers.contract.DataManager
import com.nanav.marveltest.data.model.MockCharacters
import com.nanav.marveltest.ui.characters.CharacterListDataState
import com.nanav.marveltest.ui.characters.CharacterListFlowState
import com.nanav.marveltest.ui.characters.CharacterListViewModel
import com.nanav.marveltest.util.RxSchedulersOverrideRule
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

    private lateinit var characterListViewModel: CharacterListViewModel

    private val throwable: Throwable = Throwable("Something went wrong loading data")

    @Mock
    lateinit var dataObserver: Observer<CharacterListDataState>

    @Mock
    lateinit var flowObserver: Observer<CharacterListFlowState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        `when`(dataManager.getCharacters())
            .thenReturn(Single.just(MockCharacters.ALL_CHARACTERS))

        characterListViewModel = CharacterListViewModel(dataManager)
    }

    @Test
    fun `test 1 Data Load Characters`() {
        //GIVEN
        val argumentCaptor = ArgumentCaptor.forClass(CharacterListDataState::class.java)
        characterListViewModel.characterListDataState.observeForever(dataObserver)

        //WHEN
        `when`(dataManager.getCharacters()).thenReturn(Single.just(MockCharacters.ALL_CHARACTERS))

        characterListViewModel.onCreate()

        //THEN

        argumentCaptor.run {
            verify(dataObserver, times(2)).onChanged(argumentCaptor.capture())
        }

        val values = argumentCaptor.allValues

        assertEquals(2, values.size)
        assertEquals(CharacterListDataState.DataLoading::class.java, values[0]::class.java)
        assertEquals(CharacterListDataState.ListData::class.java, values[1]::class.java)
        assertEquals(
            MockCharacters.ALL_CHARACTERS,
            (values[1] as CharacterListDataState.ListData).characters
        )
        assertEquals(
            MockCharacters.ALL_CHARACTERS,
            (values[1] as CharacterListDataState.ListData).characters
        )
    }

    @Test
    fun `test 2 Error Load Characters`() {
        //GIVEN
        val argumentCaptor = ArgumentCaptor.forClass(CharacterListDataState::class.java)
        characterListViewModel.characterListDataState.observeForever(dataObserver)

        //WHEN
        `when`(dataManager.getCharacters()).thenReturn(Single.error(throwable))

        characterListViewModel.onCreate()

        //THEN

        argumentCaptor.run {
            verify(dataObserver, times(2)).onChanged(argumentCaptor.capture())
        }

        val values = argumentCaptor.allValues

        assertEquals(2, values.size)
        assertEquals(CharacterListDataState.DataLoading::class.java, values[0]::class.java)
        assertEquals(CharacterListDataState.DataError::class.java, values[1]::class.java)
    }

    @Test
    fun `test 3 On Character clicked`() {
        //GIVEN
        val argumentCaptor = ArgumentCaptor.forClass(CharacterListFlowState::class.java)
        characterListViewModel.characterListFlowState.observeForever(flowObserver)

        //WHEN
        characterListViewModel.onCharacterClicked(MockCharacters.JOHN)

        //THEN
        argumentCaptor.run {
            verify(flowObserver, times(1)).onChanged(argumentCaptor.capture())
        }

        val values = argumentCaptor.allValues

        assertEquals(1, values.size)
        assertEquals(CharacterListFlowState.StartCharacterDetail::class.java, values[0]::class.java)
        assertEquals(
            MockCharacters.JOHN.id,
            (values[0] as CharacterListFlowState.StartCharacterDetail).characterId
        )
    }
}
