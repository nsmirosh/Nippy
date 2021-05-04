package apps.smoll.dragdropgame.features.menu

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import apps.smoll.dragdropgame.features.getOrAwaitValue
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MenuViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var mockFirestore: FirebaseRepoImpl
    private lateinit var menuViewModel: MenuViewModel

    @Before
    fun setUp() {
        mockFirestore = Mockito.mock(FirebaseRepoImpl::class.java)
        menuViewModel = MenuViewModel(mockFirestore)
    }

    @Test
    fun init_InitializesLastCompleteLevelCorrectly() {

        mockFirestore.stub {
            onBlocking { getLastLevel() }.doReturn(LevelStats(totalScore = 50))
        }

        with (menuViewModel) {
            init()
            val lastLevel = lastCompletedLevel.getOrAwaitValue()
            assertThat(lastLevel.totalScore, equalTo(50))
        }
    }
}