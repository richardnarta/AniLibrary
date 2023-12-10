package com.example.anilibrary

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.anilibrary.model.data.util.Constants.SUCCESS_JSON_RESPONSE_TEST
import com.example.anilibrary.model.network.ApiService
import com.example.anilibrary.ui.fragment.DetailFragment
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4ClassRunner::class)
class DetailFragmentTest {
    private lateinit var mockWebServer : MockWebServer
    private lateinit var apiService: ApiService
    private lateinit var scenario: FragmentScenario<DetailFragment>

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getDetail_Success() {
        val idlingResource = CountingIdlingResource("WaitForLoading")
        Espresso.registerIdlingResources(idlingResource)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(SUCCESS_JSON_RESPONSE_TEST)
        mockWebServer.enqueue(mockResponse)

        val response = runBlocking {
            apiService.getAnimeDetail(39783)
        }
        mockWebServer.takeRequest()

        Assert.assertEquals(39783, response.body()!!.id)

        val bundle = Bundle()
        response.body()!!.id?.let { bundle.putInt("id", it) }

        scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_AniLibrary
        )

        idlingResource.increment()
        Thread.sleep(5000)
        idlingResource.decrement()

        Espresso.unregisterIdlingResources(idlingResource)
    }
}