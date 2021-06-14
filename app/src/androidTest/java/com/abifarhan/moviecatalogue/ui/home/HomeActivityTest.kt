package com.abifarhan.moviecatalogue.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.abifarhan.moviecatalogue.R
import com.abifarhan.moviecatalogue.utils.DataMovie
import com.abifarhan.moviecatalogue.utils.DataTvShow
import com.abifarhan.moviecatalogue.utils.EspressoIdlingResources
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {
    private val dataMovie = DataMovie.generateMovieData()
    private val dataTvShow = DataTvShow.generateTvShowData()

    @get:Rule
    var activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(HomeActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource)
    }

    @Test
    fun loadMovie() {
        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dataMovie.size
            )
        )

    }

    @Test
    fun loadDetailMovie() {
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.textView_title_detail_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_title_detail_movie)).check(matches(withText(dataMovie[0].titleMovie)))
        onView(withId(R.id.textView_description_detail_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_description_detail_movie)).check(matches(withText(dataMovie[0].descriptionMovie)))
        onView(withId(R.id.imageView_detail_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.imageView_detail_movie)).check(matches(isDisplayed()))
    }

    @Test
    fun loadFavoriteMovie() {
        onView(withText("FILM")).perform(click())
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.action_bookmark)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withText("Favorite Movie")).perform(click())
        onView(withId(R.id.rv_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_favorite)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.textView_title_detail_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_description_detail_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.action_bookmark)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
    }

    @Test
    fun loadTv() {
        onView(withText("SERIAL")).perform(click())
        onView(withId(R.id.rv_tv_show)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dataTvShow.size
            )
        )
    }


    @Test
    fun loadDetailTv() {
        onView(withText("SERIAL")).perform(click())
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.textView_title_detail_tv_show)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_title_detail_tv_show)).check(matches(withText(dataTvShow[0].titleTv)))
        onView(withId(R.id.textView_description_detail_tv_show)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_description_detail_tv_show)).check(matches(withText(dataTvShow[0].descriptionTv)))
        onView(withId(R.id.imageView_detail_tv_show)).check(matches(isDisplayed()))
        onView(withId(R.id.imageView_detail_tv_show)).check(matches(isDisplayed()))
    }


    @Test
    fun loadFavoriteTv() {
        onView(withText("SERIAL")).perform(click())
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.actionOnItemAtPosition
            <RecyclerView.ViewHolder>(0, click())
        )
        onView(withId(R.id.action_bookmark_tv)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withText("Favorite Tv")).perform(click())
        onView(withId(R.id.rv_favorite_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_favorite_tv)).perform(
            RecyclerViewActions.actionOnItemAtPosition
            <RecyclerView.ViewHolder>(0, click())
        )
        onView(withId(R.id.textView_title_detail_tv_show)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_description_detail_tv_show)).check(matches(isDisplayed()))
        onView(withId(R.id.action_bookmark_tv)).perform(click())
        onView(isRoot()).perform(ViewActions.pressBack())
    }


}