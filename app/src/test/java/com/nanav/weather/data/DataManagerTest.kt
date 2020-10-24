package com.nanav.weather.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nanav.marveltest.BuildConfig.TEST_VERSION
import com.nanav.marveltest.data.api.CharacterService
import com.nanav.marveltest.data.managers.DataManagerImpl
import com.nanav.marveltest.data.managers.contract.DataManager
import com.nanav.marveltest.data.model.Character
import com.nanav.marveltest.data.model.CharacterListResponse
import com.nanav.marveltest.data.model.CharacterListResponseData
import com.nanav.marveltest.data.model.MockCharacters
import com.nanav.marveltest.util.JodaAndroidTimeZoneFixRule
import com.nanav.marveltest.util.RxSchedulersOverrideRule
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

    @Rule
    @JvmField
    var jodaAndroidFix = JodaAndroidTimeZoneFixRule()

    private lateinit var dataManager: DataManager

    @Mock
    lateinit var characterService: CharacterService

    @Before
    @Throws
    fun setup() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(characterService.getCharacter(MockCharacters.JOHN.id))
            .thenReturn(
                Single.just(
                    CharacterListResponse(
                        200,
                        "st",
                        CharacterListResponseData(1, listOf(MockCharacters.JOHN))
                    )
                )
            )

        Mockito.`when`(characterService.getCharacter(MockCharacters.JANE.id))
            .thenReturn(
                Single.just(
                    CharacterListResponse(
                        200,
                        "st",
                        CharacterListResponseData(0, emptyList())
                    )
                )
            )

        Mockito.`when`(characterService.getCharacter(MockCharacters.BOB.id))
            .thenReturn(
                Single.just(
                    CharacterListResponse(
                        500,
                        "st",
                        CharacterListResponseData(1, listOf(MockCharacters.BOB))
                    )
                )
            )

        dataManager = DataManagerImpl(characterService)
    }

    @Test
    fun `test Get Character Success`() {
        val testObserver = TestObserver<Character>()

        dataManager.getCharacterById(MockCharacters.JOHN.id).subscribe(testObserver)

        testObserver.awaitTerminalEvent(50, TimeUnit.MILLISECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]

        assertEquals(result.id, MockCharacters.JOHN.id)
        assertEquals(result.name, MockCharacters.JOHN.name)
        assertEquals(result.description, MockCharacters.JOHN.description)
        assertEquals(result.thumbnail, MockCharacters.JOHN.thumbnail)
    }

    @Test
    fun `test Get Character Error 1`() {
        val testObserver = TestObserver<Character>()

        dataManager.getCharacterById(MockCharacters.JANE.id).subscribe(testObserver)

        testObserver.awaitTerminalEvent(50, TimeUnit.MILLISECONDS)
        assertEquals(1, testObserver.errorCount())
    }

    @Test
    fun `test Get Character Error 2`() {
        val testObserver = TestObserver<Character>()

        dataManager.getCharacterById(MockCharacters.BOB.id).subscribe(testObserver)

        testObserver.awaitTerminalEvent(50, TimeUnit.MILLISECONDS)
        assertEquals(1, testObserver.errorCount())
    }
}
