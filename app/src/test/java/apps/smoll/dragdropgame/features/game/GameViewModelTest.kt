package apps.smoll.dragdropgame.features.game

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import apps.smoll.dragdropgame.Shape
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
    fun startGame_buildsShapes() {

        val gameViewModel = GameViewModel(ApplicationProvider.getApplicationContext())

        val screenShapesObserver = Observer<List<Shape>> {}
        val levelTextObserver = Observer<String> {}

        try {

            gameViewModel.screenShapes.observeForever(screenShapesObserver)
            gameViewModel.levelText.observeForever(levelTextObserver)
            gameViewModel.startGame(Pair(1080, 1920))
            val screenShapesValue = gameViewModel.screenShapes.value
            val levelTextValue = gameViewModel.levelText.value

            assertThat(screenShapesValue, (not(nullValue())))
            assertThat(screenShapesValue?.size, equalTo(1))
            assertThat(levelTextValue, equalTo("Level: 1"))

            gameViewModel.handleMatchingShapeDrop(screenShapesValue!![0].topLeftCoords)

            val screenShapesValue = gameViewModel.screenShapes.value
            val levelTextValue = gameViewModel.levelText.value



        } finally {
            gameViewModel.screenShapes.removeObserver(screenShapesObserver)
            gameViewModel.levelText.removeObserver(levelTextObserver)
        }
    }
}