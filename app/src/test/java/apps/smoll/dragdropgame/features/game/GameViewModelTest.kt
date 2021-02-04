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

        val observer = Observer<List<Shape>> {}
        try {

            gameViewModel.screenShapes.observeForever(observer)
            gameViewModel.startGame(Pair(1080, 1920))
            val value = gameViewModel.screenShapes.value
            assertThat(value, (not(nullValue())))
            assertThat(value?.size, equalTo(1))


        } finally {
            gameViewModel.screenShapes.removeObserver(observer)
        }
    }
}