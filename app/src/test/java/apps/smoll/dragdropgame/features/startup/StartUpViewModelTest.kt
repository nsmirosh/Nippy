package apps.smoll.dragdropgame.features.startup

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import apps.smoll.dragdropgame.features.auth.AuthActivity
import apps.smoll.dragdropgame.features.game.MainActivity
import apps.smoll.dragdropgame.features.getOrAwaitValue
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StartUpViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var startUpViewModel: StartUpViewModel
    private lateinit var firebaseUtils: FirebaseUtils

    @Before
    fun setUp() {
        firebaseUtils = Mockito.mock(FirebaseUtils::class.java)
        startUpViewModel = StartUpViewModel(firebaseUtils)
    }

    @Test
    fun onStart_withUser_redirectsToMainActivity() = with (startUpViewModel){
        `when`(firebaseUtils.isAuthenticated()).thenReturn(true)
        onStart()
        val value = launchScreenEvent.getOrAwaitValue().getContentIfNotHandled()
        assertThat(MainActivity::class.java, equalTo(value))
    }

    @Test
    fun onStart_withoutUser_redirectsToAuthActivity() = with (startUpViewModel){
        `when`(firebaseUtils.isAuthenticated()).thenReturn(false)
        onStart()
        val value = launchScreenEvent.getOrAwaitValue().getContentIfNotHandled()
        assertThat(AuthActivity::class.java, equalTo(value))
    }
}