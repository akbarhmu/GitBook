package id.ngode.gitbook

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(SettingActivity::class.java)
    }

    @Test
    fun darkModeSwitch(){
        onView(withId(R.id.switch_theme)).perform(click())
    }
}