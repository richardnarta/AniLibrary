package com.example.anilibrary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.anilibrary.model.data.database.AnimeListEntity
import com.example.anilibrary.model.data.repository.AnimeListRepository
import com.example.anilibrary.viewmodel.ListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var animeListRepository: AnimeListRepository

    private lateinit var viewModel: ListViewModel

    @Before
    fun setup() {
        // [Setup code remains the same as before]
        Dispatchers.setMain(testDispatcher)
        val mockAnimeList = listOf(
            AnimeListEntity(
                id = 1,
                listType = "watched",
                animePoster = "http://example.com/poster1.jpg",
                animeTitle = "Anime Title 1",
                animeSeason = "Winter",
                animeYear = 2021,
                animeRating = 8.5f
            ),
            AnimeListEntity(
                id = 2,
                listType = "planned",
                animePoster = "http://example.com/poster2.jpg",
                animeTitle = "Anime Title 2",
                animeSeason = "Spring",
                animeYear = 2022,
                animeRating = 9.0f
            ),
            AnimeListEntity(
                id = 3,
                listType = "watched",
                animePoster = "http://example.com/poster3.jpg",
                animeTitle = "Anime Title 3",
                animeSeason = "Summer",
                animeYear = 2020,
                animeRating = 7.8f
            ),
            AnimeListEntity(
                id = 4,
                listType = "planned",
                animePoster = "http://example.com/poster4.jpg",
                animeTitle = "Anime Title 4",
                animeSeason = "Fall",
                animeYear = 2023,
                animeRating = 8.2f
            ),
            AnimeListEntity(
                id = 5,
                listType = "watched",
                animePoster = "http://example.com/poster5.jpg",
                animeTitle = "Anime Title 5",
                animeSeason = "Winter",
                animeYear = 2019,
                animeRating = 7.5f
            ),
            AnimeListEntity(
                id = 6,
                listType = "planned",
                animePoster = "http://example.com/poster6.jpg",
                animeTitle = "Anime Title 6",
                animeSeason = "Spring",
                animeYear = 2021,
                animeRating = 8.7f
            )
        )

        whenever(animeListRepository.getWatchedAnimeByQuery(anyString())).thenReturn(flowOf(mockAnimeList))
        whenever(animeListRepository.getPlannedAnimeByQuery(anyString())).thenReturn(flowOf(mockAnimeList))
        whenever(animeListRepository.getAllAnimeByQuery(anyString())).thenReturn(flowOf(mockAnimeList))

        viewModel = ListViewModel(animeListRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `showAllAnimeByQuery calls repository with correct query`() = runTest(testDispatcher) {
        val query = "query"
        viewModel.showAllAnimeByQuery(query) { }
        verify(animeListRepository).getAllAnimeByQuery(query)
    }

    @Test
    fun `showWatchedAnimeByQuery calls repository with correct query`() = runTest(testDispatcher) {
        val query = "watched query"
        viewModel.showWatchedAnimeByQuery(query) { }
        verify(animeListRepository).getWatchedAnimeByQuery(query)
    }

    @Test
    fun `showPlannedAnimeByQuery calls repository with correct query`() = runTest(testDispatcher) {
        val query = "planned query"
        viewModel.showPlannedAnimeByQuery(query) { }
        verify(animeListRepository).getPlannedAnimeByQuery(query)
    }

}
