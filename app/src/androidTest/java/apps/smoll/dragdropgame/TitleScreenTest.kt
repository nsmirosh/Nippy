package apps.smoll.dragdropgame

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import apps.smoll.dragdropgame.features.menu.MenuFragment
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TitleScreenTest {

    private lateinit var navController: NavController

    @Before
    fun setUp() {
        // Create a TestNavHostController
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        // Create a graphical FragmentScenario for the TitleScreen
        val menuFragmentScenario = launchFragmentInContainer<MenuFragment>()

        menuFragmentScenario.onFragment { fragment ->
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.nav_graph)

            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }

    @Test
    fun testNavigationToStatsScreen() {
        onView(ViewMatchers.withId(R.id.statsButton)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id, equalTo(R.id.statsFragment))
    }

    @Test
    fun testNavigationToInGameScreen() {
        onView(ViewMatchers.withId(R.id.startNewGameButton)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id, equalTo(R.id.gameFragment))
    }
}