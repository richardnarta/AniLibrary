package com.example.anilibrary

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.anilibrary.ui.fragment.HomeFragment
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {
    private lateinit var scenario: FragmentScenario<HomeFragment>

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup(){
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_AniLibrary)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun loadPagingAnime(){
        val idlingResource = CountingIdlingResource("WaitForLoading")
        Espresso.registerIdlingResources(idlingResource)

        idlingResource.increment()
        Thread.sleep(5000)
        idlingResource.decrement()

        onView(withId(R.id.rvAnimeCard)).check(matches(isDisplayed()))

        onView(withId(R.id.rvAnimeCard))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(9))

        idlingResource.increment()
        Thread.sleep(5000)
        idlingResource.decrement()

        onView(withId(R.id.rvAnimeCard))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(19))

        idlingResource.increment()
        Thread.sleep(5000)
        idlingResource.decrement()

        onView(withId(R.id.rvAnimeCard))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(29))

        idlingResource.increment()
        Thread.sleep(2000)
        idlingResource.decrement()

        Espresso.unregisterIdlingResources(idlingResource)
    }

    @Test
    fun changeSpinner(){
        val idlingResource = CountingIdlingResource("WaitForLoading")
        Espresso.registerIdlingResources(idlingResource)
        onView(withId(R.id.tvHomeTitle)).check(matches(withText("Fall - 2023")))

        val itemToSelect = "Winter 2023  "
        onView(withId(R.id.spinnerSeason)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerSeason)).perform(click())

        idlingResource.increment()
        Thread.sleep(1000)
        idlingResource.decrement()

        onData(allOf(`is`(instanceOf(String::class.java)), `is`(itemToSelect)))
            .inRoot(isPlatformPopup())
            .perform(click())

        idlingResource.increment()
        Thread.sleep(3000)
        idlingResource.decrement()

        onView(withId(R.id.tvHomeTitle)).check(matches(withText("Winter - 2023")))

        Espresso.unregisterIdlingResources(idlingResource)
    }
}
