package com.example.myapplication

import android.content.pm.ActivityInfo
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
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

    fun isDisplayedCheck(id: Int) {
        onView(withId(id)).check(matches(isDisplayed()))
    }

    fun doesNotExistsCheck(id: Int) {
        onView(withId(id)).check(doesNotExist())
    }

    fun clickButton(id: Int) {
        onView(withId(id)).perform(click())
    }

    fun firstFragmentExists() {
        isDisplayedCheck(R.id.activity_main)
        isDisplayedCheck(R.id.fragment1)
        doesNotExistsCheck(R.id.bnToFirst)
        isDisplayedCheck(R.id.bnToSecond)
        doesNotExistsCheck(R.id.bnToThird)
    }

    fun secondFragmentExists() {
        isDisplayedCheck(R.id.activity_main)
        isDisplayedCheck(R.id.fragment2)
        isDisplayedCheck(R.id.bnToFirst)
        doesNotExistsCheck(R.id.bnToSecond)
        isDisplayedCheck(R.id.bnToThird)
    }

    fun thirdFragmentExists() {
        isDisplayedCheck(R.id.activity_main)
        isDisplayedCheck(R.id.fragment3)
        isDisplayedCheck(R.id.bnToFirst)
        isDisplayedCheck(R.id.bnToSecond)
        doesNotExistsCheck(R.id.bnToThird)
    }

    fun aboutExists() {
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

}