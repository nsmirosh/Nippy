package apps.smoll.dragdropgame.features.game.inbetween

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import apps.smoll.dragdropgame.features.getOrAwaitValue
import apps.smoll.dragdropgame.repository.LevelStats
import com.google.firebase.firestore.FirebaseFirestore
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
class InBetweenViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var inBetweenViewModel: InBetweenViewModel

    @Before
    fun setUp() {
        inBetweenViewModel = InBetweenViewModel()
    }

    @Test
    fun initWithArgs_withValidParams_initLiveDataProperly() {
        val stats = LevelStats(totalTimeInMillis = 123456)
        with(inBetweenViewModel) {
            initWithArgs(stats)
            val score = score.getOrAwaitValue()
            val levelStats = levelStats.getOrAwaitValue()

            assertThat(score, equalTo("02:03:456"))
            assertThat(levelStats, equalTo(stats))
        }
    }
}