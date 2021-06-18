package apps.smoll.dragdropgame.features.game

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import apps.smoll.dragdropgame.features.getOrAwaitValue
import apps.smoll.dragdropgame.halfShapeSize
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.minus
import apps.smoll.dragdropgame.utils.permissibleHitFaultInPixels
import apps.smoll.dragdropgame.utils.plus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.*
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
        gameViewModel = GameViewModel(FirebaseRepoImpl(mockFirestore))
    }

    @Test
    fun startGame_withNoPreexistingLevelData_buildsShapesAndDisplaysInitialData() {

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
    fun handleMatchingShapeDrop_withCoordsMatchingExisting_removesShape() {

        with(gameViewModel) {
            startGame(1080, 1920)
            val screenShapesValue = screenShapes.getOrAwaitValue()

            // when we "drop" the matching shape over the shape on screen
            // the actual drop coordinates are the center of the view we drag - hence we need to adjust for halfShapeSize

            val dropCoords = screenShapesValue.first().topLeftCoords + halfShapeSize
            handleMatchingShapeDrop(dropCoords)
            val screenShapesValue2 = screenShapes.getOrAwaitValue()
            assertThat(screenShapesValue2.size, equalTo(0))
        }
    }


    @Test
    fun handleMatchingShapeDrop_withCoordsAwayFromExisting_movesToNewCoords() {

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
    fun handleMatchingShapeDrop_withShapeBeingHit_writesTheDataToFirestore() {


        val repo = Mockito.mock(FirebaseRepoImpl::class.java)
        val gameViewModel = GameViewModel(repo)

        with(gameViewModel) {
            startGame(1080, 1920)
            val screenShapesValue = screenShapes.getOrAwaitValue()
            val dropCoords = screenShapesValue.first().topLeftCoords + halfShapeSize
            handleMatchingShapeDrop(dropCoords)

            val argument = argumentCaptor<LevelStats>()

            runBlocking {
                verify(repo).writeLevelStats(argument.capture())
            }

            with(argument.firstValue) {
                assertThat("currentLevel", levelToBePlayed, equalTo(2))
                assertThat("wonCurrentLevel", wonCurrentLevel, equalTo(true))
                assertThat("levelTimeInMillis", levelTimeInMillis, lessThan(1000))
            }
        }
    }


    @Test
    fun startGame_withPreviousLevelData_initializesFieldsCorrectly() {

        val levelStats = LevelStats(
            levelToBePlayed = 3,
            totalTimeInMillis = 1000
        )

        with(gameViewModel) {
            startGame(1080, 1920, levelStats)
            val currentLevel = currentLevel.getOrAwaitValue()
            val screenShapes = screenShapes.getOrAwaitValue()
            assertThat(currentLevel, equalTo(3))
            assertThat(screenShapes.size, equalTo(3))
        }
    }


    @Test
    fun startGame_withTimeRunningOut_runsPlayerFailsCallback() {

        val levelStats = LevelStats(
            levelToBePlayed = 3,
            totalTimeInMillis = 1000
        )

        with ()

        Intent().apply {
            putExtras(this)
        }

        with(gameViewModel) {
            startGame(1080, 1920, levelStats)
//            Thread.sleep(4000)
//            val currentLevel = currentLevel.getOrAwaitValue()
            val screenShapesRes1 = screenShapes.getOrAwaitValue()
            assertThat(screenShapesRes1.size, equalTo(3))

            after(4000)
            val screenShapesRes2 = screenShapes.getOrAwaitValue()
            assertThat(screenShapesRes2.size, equalTo(0))
//            assertThat(currentLevel, equalTo(3))
        }
    }
}