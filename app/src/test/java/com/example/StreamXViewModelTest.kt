package com.example

import com.example.data.SampleCatalog
import com.example.data.StreamXRepository
import com.example.ui.NavigationTab
import com.example.ui.StreamXViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StreamXViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialCatalogAndTabs() {
        val repo = StreamXRepository()
        val viewModel = StreamXViewModel(repo)

        assertEquals(NavigationTab.HOME, viewModel.currentTab.value)
        assertFalse(viewModel.isPlayerOpen.value)
        assertEquals(6, viewModel.catalog.value.size)

        // Select search tab
        viewModel.setTab(NavigationTab.SEARCH)
        assertEquals(NavigationTab.SEARCH, viewModel.currentTab.value)
    }

    @Test
    fun testOpenPlayerAndSelectSeason() {
        val repo = StreamXRepository()
        val viewModel = StreamXViewModel(repo)

        val featured = SampleCatalog.featuredShow
        viewModel.openPlayer(featured)

        assertTrue(viewModel.isPlayerOpen.value)
        assertEquals(featured.id, viewModel.currentPlayingShow.value.id)

        // Select season 1
        viewModel.selectSeason(1)
        assertEquals(1, viewModel.selectedSeasonNumber.value)
        assertEquals(1, viewModel.currentPlayingEpisode.value.episodeNumber)

        // Close player
        viewModel.closePlayer()
        assertFalse(viewModel.isPlayerOpen.value)
    }

    @Test
    fun testMyListToggle() {
        val repo = StreamXRepository()
        val viewModel = StreamXViewModel(repo)

        val showId = SampleCatalog.movieInterstellar.id
        assertFalse(viewModel.myListIds.value.contains(showId))

        viewModel.toggleMyList(showId)
        assertTrue(viewModel.myListIds.value.contains(showId))

        viewModel.toggleMyList(showId)
        assertFalse(viewModel.myListIds.value.contains(showId))
    }

    @Test
    fun testSearchFiltering() {
        val repo = StreamXRepository()
        val viewModel = StreamXViewModel(repo)

        viewModel.setSearchQuery("Solo")
        testDispatcher.scheduler.advanceUntilIdle()

        val results = viewModel.searchResults.value
        assertEquals(1, results.size)
        assertEquals("Solo Leveling: Arise", results.first().title)
    }
}
