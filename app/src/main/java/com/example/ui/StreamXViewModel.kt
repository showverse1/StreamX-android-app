package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DownloadItem
import com.example.data.Episode
import com.example.data.Show
import com.example.data.StreamXRepository
import com.example.data.UserProfile
import com.example.data.WatchHistoryItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab {
    HOME,
    SEARCH,
    OFFLINE,
    ME
}

class StreamXViewModel(
    private val repository: StreamXRepository = StreamXRepository()
) : ViewModel() {

    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _isPlayerOpen = MutableStateFlow(false)
    val isPlayerOpen: StateFlow<Boolean> = _isPlayerOpen.asStateFlow()

    private val _isFirestoreLoading = MutableStateFlow(true)
    val isFirestoreLoading: StateFlow<Boolean> = _isFirestoreLoading.asStateFlow()

    val catalog: StateFlow<List<Show>> = repository.catalog
    val myListIds: StateFlow<Set<String>> = repository.myListIds
    val downloads: StateFlow<List<DownloadItem>> = repository.downloads
    val watchHistory: StateFlow<List<WatchHistoryItem>> = repository.watchHistory
    val profile: StateFlow<UserProfile> = repository.profile
    val currentPlayingShow: StateFlow<Show> = repository.currentPlayingShow
    val selectedSeasonNumber: StateFlow<Int> = repository.selectedSeasonNumber
    val currentPlayingEpisode: StateFlow<Episode> = repository.currentPlayingEpisode
    val unreadNotifications: StateFlow<Int> = repository.unreadNotificationsCount

    // Home category filter
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredHomeCatalog: StateFlow<List<Show>> = combine(catalog, selectedCategory) { list, cat ->
        if (cat == "All") list else list.filter { it.category.equals(cat, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSearchGenre = MutableStateFlow("All")
    val selectedSearchGenre: StateFlow<String> = _selectedSearchGenre.asStateFlow()

    // Recent Searches persisted via SearchPreferencesRepository
    private var searchPrefsRepo: com.example.data.SearchPreferencesRepository? = null

    private val _recentSearches = MutableStateFlow(
        listOf("Solo Leveling", "Queen of Tears", "Demon Slayer", "Cyberpunk")
    )
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    fun attachSearchPrefsRepo(repo: com.example.data.SearchPreferencesRepository) {
        searchPrefsRepo = repo
        viewModelScope.launch {
            repo.recentSearches.collect {
                _recentSearches.value = it
            }
        }
    }

    fun addRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return
        searchPrefsRepo?.addQuery(trimmed) ?: run {
            val list = _recentSearches.value.toMutableList()
            list.removeAll { it.equals(trimmed, ignoreCase = true) }
            list.add(0, trimmed)
            _recentSearches.value = list.take(10)
        }
    }

    fun removeRecentSearch(query: String) {
        searchPrefsRepo?.removeQuery(query) ?: run {
            _recentSearches.value = _recentSearches.value.filterNot { it.equals(query, ignoreCase = true) }
        }
    }

    fun clearAllRecentSearches() {
        searchPrefsRepo?.clearAll() ?: run {
            _recentSearches.value = emptyList()
        }
    }

    // Firebase Auth State & Watch History Personalization
    val authUserState: StateFlow<com.example.data.AuthUserState?> = com.example.data.FirebaseAuthService.currentUserState

    private val _showAuthBottomSheet = MutableStateFlow(false)
    val showAuthBottomSheet: StateFlow<Boolean> = _showAuthBottomSheet.asStateFlow()

    fun setAuthBottomSheet(show: Boolean) {
        _showAuthBottomSheet.value = show
    }

    fun onUserAuthenticated(user: com.example.data.AuthUserState) {
        repository.syncUserWithCloud(user.uid, user.email, user.displayName)
    }

    fun updateUserProfile(displayName: String, photoUrl: String?) {
        viewModelScope.launch {
            com.example.data.FirebaseAuthService.updateProfile(displayName, photoUrl)
            repository.updateUserProfile(displayName, photoUrl)
        }
    }

    val searchResults: StateFlow<List<Show>> = combine(catalog, _searchQuery, _selectedSearchGenre) { list, query, genre ->
        list.filter { show ->
            val matchesQuery = query.isBlank() ||
                show.title.contains(query, ignoreCase = true) ||
                show.genres.any { it.contains(query, ignoreCase = true) } ||
                show.category.contains(query, ignoreCase = true) ||
                show.description.contains(query, ignoreCase = true)
            val matchesGenre = genre == "All" ||
                show.genres.any { it.equals(genre, ignoreCase = true) } ||
                show.category.equals(genre, ignoreCase = true)
            matchesQuery && matchesGenre
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Modals & Sheets
    private val _showSeasonBottomSheet = MutableStateFlow(false)
    val showSeasonBottomSheet: StateFlow<Boolean> = _showSeasonBottomSheet.asStateFlow()

    private val _showAudioSubtitleSheet = MutableStateFlow(false)
    val showAudioSubtitleSheet: StateFlow<Boolean> = _showAudioSubtitleSheet.asStateFlow()

    private val _showCreatorStudioSheet = MutableStateFlow(false)
    val showCreatorStudioSheet: StateFlow<Boolean> = _showCreatorStudioSheet.asStateFlow()

    private val _showCastDialog = MutableStateFlow(false)
    val showCastDialog: StateFlow<Boolean> = _showCastDialog.asStateFlow()

    private val _showNotificationSheet = MutableStateFlow(false)
    val showNotificationSheet: StateFlow<Boolean> = _showNotificationSheet.asStateFlow()

    // Player settings & audio track
    private val _selectedAudioTrack = MutableStateFlow("Japanese [Original] (Dolby Atmos)")
    val selectedAudioTrack: StateFlow<String> = _selectedAudioTrack.asStateFlow()

    private val _selectedSubtitleTrack = MutableStateFlow("English [CC]")
    val selectedSubtitleTrack: StateFlow<String> = _selectedSubtitleTrack.asStateFlow()

    // Initial load and simulated download progression
    init {
        viewModelScope.launch {
            delay(150)
            _isFirestoreLoading.value = false
        }

        viewModelScope.launch {
            while (true) {
                delay(1200)
                val activeList = repository.downloads.value
                val downloadingItem = activeList.firstOrNull { it.status == com.example.data.DownloadStatus.DOWNLOADING && it.progress < 1.0f }
                if (downloadingItem != null) {
                    val nextProgress = (downloadingItem.progress + 0.08f).coerceAtMost(1.0f)
                    if (nextProgress >= 1.0f) {
                        repository.completeDownload(downloadingItem.id)
                    }
                }
            }
        }
    }

    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun refreshFromFirestore() {
        viewModelScope.launch {
            _isFirestoreLoading.value = true
            delay(350)
            _isFirestoreLoading.value = false
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchGenre(genre: String) {
        _selectedSearchGenre.value = genre
    }

    fun openPlayer(show: Show, seasonNumber: Int? = null, episode: Episode? = null) {
        repository.selectShow(show, seasonNumber, episode)
        _isPlayerOpen.value = true
    }

    fun closePlayer() {
        _isPlayerOpen.value = false
    }

    fun toggleMyList(showId: String) {
        repository.toggleMyList(showId)
    }

    fun selectSeason(seasonNumber: Int) {
        repository.selectSeason(seasonNumber)
        _showSeasonBottomSheet.value = false
    }

    fun selectEpisode(episode: Episode) {
        repository.selectEpisode(episode)
    }

    fun setSeasonBottomSheet(open: Boolean) {
        _showSeasonBottomSheet.value = open
    }

    fun setAudioSubtitleSheet(open: Boolean) {
        _showAudioSubtitleSheet.value = open
    }

    fun setCreatorStudioSheet(open: Boolean) {
        _showCreatorStudioSheet.value = open
    }

    fun setCastDialog(open: Boolean) {
        _showCastDialog.value = open
    }

    fun setNotificationSheet(open: Boolean) {
        _showNotificationSheet.value = open
        if (open) {
            repository.clearNotifications()
        }
    }

    fun setAudioTrack(track: String) {
        _selectedAudioTrack.value = track
    }

    fun setSubtitleTrack(sub: String) {
        _selectedSubtitleTrack.value = sub
    }

    fun downloadCurrentEpisode() {
        val show = currentPlayingShow.value
        val episode = currentPlayingEpisode.value
        repository.startDownload(show, episode)
    }

    fun toggleDownloadPause(downloadId: String) {
        repository.toggleDownloadPause(downloadId)
    }

    fun deleteDownload(downloadId: String) {
        repository.deleteDownload(downloadId)
    }

    fun toggleWifiOnly() {
        repository.toggleWifiOnly()
    }

    fun toggleSmartDownloads() {
        repository.toggleSmartDownloads()
    }

    fun clearCache() {
        repository.clearCache()
    }
}
