package apps.smoll.dragdropgame.features.game

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.utils.getOrAwaitValue
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class GameViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun startGame_buildsShapesAndDisplaysInitialData() {

        val gameViewModel = GameViewModel(ApplicationProvider.getApplicationContext())


        with (gameViewModel) {
            startGame(1080, 1920)
            val screenShapesValue = screenShapes.getOrAwaitValue()
            val levelTextValue = levelText.getOrAwaitValue()

            assertThat(screenShapesValue, (not(nullValue())))
            assertThat(screenShapesValue.size, equalTo(1))
            assertThat(levelTextValue, equalTo("Level: 1"))
        }

    }

    @Test
    fun handleDrop_advancesToThe2ndLevel() {

        val gameViewModel = GameViewModel(ApplicationProvider.getApplicationContext())


        with (gameViewModel) {
            startGame(1080, 1920)
            val screenShapesValue = screenShapes.getOrAwaitValue()
            handleMatchingShapeDrop(screenShapesValue[0].topLeftCoords)
            val screenShapesValue2 = screenShapes.getOrAwaitValue()
            assertThat(screenShapesValue2.size, equalTo(2))
        }

    }
}