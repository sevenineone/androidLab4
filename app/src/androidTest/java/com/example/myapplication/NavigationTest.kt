package com.example.myapplication

import android.content.pm.ActivityInfo
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAbout() {
        launchActivity<MainActivity>()
        openAbout()
        onView(withId(R.id.activity_about))
            .check(matches(isDisplayed()))
    }

    private fun isDisplayedCheck(id: Int) {
        onView(withId(id)).check(matches(isDisplayed()))
    }

    private fun doesNotExistsCheck(id: Int) {
        onView(withId(id)).check(doesNotExist())
    }

    private fun clickButton(id: Int) {
        onView(withId(id)).perform(click())
    }

    private fun firstFragmentExists() {
        isDisplayedCheck(R.id.activity_main)
        isDisplayedCheck(R.id.fragment1)
        doesNotExistsCheck(R.id.bnToFirst)
        isDisplayedCheck(R.id.bnToSecond)
        doesNotExistsCheck(R.id.bnToThird)
    }

    private fun secondFragmentExists() {
        isDisplayedCheck(R.id.activity_main)
        isDisplayedCheck(R.id.fragment2)
        isDisplayedCheck(R.id.bnToFirst)
        doesNotExistsCheck(R.id.bnToSecond)
        isDisplayedCheck(R.id.bnToThird)
    }

    private fun thirdFragmentExists() {
        isDisplayedCheck(R.id.activity_main)
        isDisplayedCheck(R.id.fragment3)
        isDisplayedCheck(R.id.bnToFirst)
        isDisplayedCheck(R.id.bnToSecond)
        doesNotExistsCheck(R.id.bnToThird)
    }

    private fun aboutExists() {
        doesNotExistsCheck(R.id.activity_main)
        isDisplayedCheck(R.id.activity_about)
        isDisplayedCheck(R.id.tvAbout)
    }

    @Test
    fun firstFragmentNavigationTest() { //1
        firstFragmentExists()
        clickButton(R.id.bnToSecond) //2
        secondFragmentExists()
        clickButton(R.id.bnToFirst) //1
        openAbout() //about
        aboutExists()
        pressBack() //1
        firstFragmentExists()
    }

    @Test
    fun secondFragmentNavigationTest() { //1
        firstFragmentExists()
        clickButton(R.id.bnToSecond) //2
        secondFragmentExists()
        clickButton(R.id.bnToThird) //3
        thirdFragmentExists()
        clickButton(R.id.bnToSecond) //2
        secondFragmentExists()
        openAbout() //about
        aboutExists()
        pressBack() //2
        secondFragmentExists()
    }

    @Test
    fun thirdFragmentNavigationTest() { //1
        firstFragmentExists()
        clickButton(R.id.bnToSecond) //2
        secondFragmentExists()
        clickButton(R.id.bnToThird) //3
        thirdFragmentExists()
        clickButton(R.id.bnToFirst) //1
        firstFragmentExists()
        clickButton(R.id.bnToSecond) //2
        clickButton(R.id.bnToThird) //3
        thirdFragmentExists()
        openAbout() //about
        aboutExists()
        pressBack() //3
        thirdFragmentExists()
    }


    @Test
    fun backStackTest() { //1
        clickButton(R.id.bnToSecond) //2
        clickButton(R.id.bnToThird) //3
        clickButton(R.id.bnToFirst) //1
        openAbout()
        pressBack()//1
        clickButton(R.id.bnToSecond) //2
        clickButton(R.id.bnToThird) //3
        clickButton(R.id.bnToFirst) //1
        clickButton(R.id.bnToSecond) //2
        clickButton(R.id.bnToFirst) //1
        clickButton(R.id.bnToSecond) //2
        clickButton(R.id.bnToThird) //3
        openAbout()
        pressBack() //3
        openAbout()
        pressBack() //3
        pressBack() //2
        pressBack() //1
        pressBackUnconditionally()
        assertTrue(activityRule.scenario.state.isAtLeast(Lifecycle.State.DESTROYED))
    }

    @Test
    fun exitFromFirstFragmentBackStackTest() {
        pressBackUnconditionally()
        assertTrue(activityRule.scenario.state.isAtLeast(Lifecycle.State.DESTROYED))
    }

    @Test
    fun exitFromSecondFragmentBackStackTest() {
        clickButton(R.id.bnToSecond)
        pressBack()
        firstFragmentExists()
        pressBackUnconditionally()
        assertTrue(activityRule.scenario.state.isAtLeast(Lifecycle.State.DESTROYED))
    }

    @Test
    fun exitFromThirdFragmentBackStackTest() {
        clickButton(R.id.bnToSecond)
        clickButton(R.id.bnToThird)
        pressBack()
        secondFragmentExists()
        pressBack()
        firstFragmentExists()
        pressBackUnconditionally()
        assertTrue(activityRule.scenario.state.isAtLeast(Lifecycle.State.DESTROYED))
    }

    @Test
    fun upNavigationTest() {
        firstFragmentExists() //1
        openAbout() //about
        aboutExists()
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)) //1
            .perform(click())

        firstFragmentExists()
        clickButton(R.id.bnToSecond) //2
        secondFragmentExists()
        openAbout() //about
        aboutExists()
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)) //2
            .perform(click())

        secondFragmentExists()
        clickButton(R.id.bnToThird) //3
        thirdFragmentExists()
        openAbout() //about
        aboutExists()
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)) //3
            .perform(click())

        thirdFragmentExists()
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)) //2
            .perform(click())

        secondFragmentExists()
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)) //1
            .perform(click())

        firstFragmentExists()
        try {
            onView(withContentDescription(R.string.nav_app_bar_navigate_up_description))
                .perform(click())
            assert(false)
        } catch (NoActivityResumedException: Exception) {
            assert(true)
        }

    }

    @Test
    fun firstFragmentRotationTest() {
        firstFragmentExists()
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        firstFragmentExists()
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
        firstFragmentExists()
    }

    @Test
    fun secondFragmentRotationTest() {
        clickButton(R.id.bnToSecond)
        secondFragmentExists()
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        secondFragmentExists()
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        Thread.sleep(1000)
        secondFragmentExists()
    }

    @Test
    fun thirdFragmentRotationTest() {
        clickButton(R.id.bnToSecond)
        clickButton(R.id.bnToThird)
        thirdFragmentExists()
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        thirdFragmentExists()
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        thirdFragmentExists()
    }

    @Test
    fun aboutRotationTest() {
        openAbout() //about
        aboutExists()
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        Thread.sleep(1000)
        aboutExists()
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        aboutExists()
    }

}