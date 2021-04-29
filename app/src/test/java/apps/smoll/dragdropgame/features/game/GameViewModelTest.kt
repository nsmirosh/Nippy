package apps.smoll.dragdropgame.features.game

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import apps.smoll.dragdropgame.features.getOrAwaitValue
import apps.smoll.dragdropgame.halfShapeSize
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.minus
import apps.smoll.dragdropgame.utils.permissibleHitFaultInPixels
import apps.smoll.dragdropgame.utils.plus
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class GameViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var gameViewModel: GameViewModel

    @Before
    fun setUp() {
        mockFirestore = Mockito.mock(FirebaseFirestore::class.java)
        gameViewModel = GameViewModel(
            ApplicationProvider.getApplicationContext(),
            FirebaseRepoImpl(mockFirestore)
        )
    }


    @Test
    fun startGame_buildsShapesAndDisplaysInitialData() {

        with(gameViewModel) {
            startGame(1080, 1920)
            val screenShapesValue = screenShapes.getOrAwaitValue()
            val levelTextValue = currentLevel.getOrAwaitValue()

            assertThat(screenShapesValue, (not(nullValue())))
            assertThat(screenShapesValue.size, equalTo(1))
            assertThat(levelTextValue, equalTo(1))
        }
    }

    @Test
    fun handleDrop_removesShapeOnHit() {

        with(gameViewModel) {
            startGame(1080, 1920)
            val screenShapesValue = screenShapes.getOrAwaitValue()

            // when we "drop" the matching shape over the shape on screen
            // the actual drop coordinates are the center of the view we drag - hence we need to adjust for halfShapeSize

            val dropCoords = screenShapesValue[0].topLeftCoords + halfShapeSize
            handleMatchingShapeDrop(dropCoords)
            val screenShapesValue2 = screenShapes.getOrAwaitValue()
            assertThat(screenShapesValue2.size, equalTo(0))
        }
    }


    @Test
    fun handleDrop_movesTheMatchingShapeToNewCoords() {

        with(gameViewModel) {
            startGame(1080, 1920)
            val screenShapesValue = screenShapes.getOrAwaitValue()
            assertThat(screenShapesValue.size, equalTo(1))

            // when we "drop" the matching shape over the shape on screen
            // the actual drop coordinates are the center of the view we drag - hence we need to adjust for halfShapeSize

            val dropCoords = screenShapesValue[0].topLeftCoords + permissibleHitFaultInPixels * 3
            handleMatchingShapeDrop(dropCoords)
            val updatedScreenShapes = screenShapes.getOrAwaitValue()
            val matchingShape = shapeToMatch.getOrAwaitValue()
            assertThat(updatedScreenShapes.size, equalTo(1))

            //the matching shape is adjusted by halfShapeSize because it needs to be centered on screen
            assertThat(matchingShape.topLeftCoords, equalTo(dropCoords - halfShapeSize))
        }
    }


    @Test
    fun startGame_withPreviousLevelData_initializesFieldsCorrectly() {

        val levelStats = LevelStats(
            level = 3,
            totalScore = 5
        )

        with(gameViewModel) {
            startGame(1080, 1920, levelStats)
            val currentLevel = currentLevel.getOrAwaitValue()
            assertThat(currentLevel, equalTo(3))
        }
    }

}