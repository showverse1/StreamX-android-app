package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StreamXRepository {
    private val _catalog = MutableStateFlow(SampleCatalog.allCatalog)
    val catalog: StateFlow<List<Show>> = _catalog.asStateFlow()

    private val _myListIds = MutableStateFlow<Set<String>>(setOf(SampleCatalog.featuredShow.id, SampleCatalog.kdramaQueenOfTears.id))
    val myListIds: StateFlow<Set<String>> = _myListIds.asStateFlow()

    private val _downloads = MutableStateFlow(SampleCatalog.initialDownloads)
    val downloads: StateFlow<List<DownloadItem>> = _downloads.asStateFlow()

    private val _watchHistory = MutableStateFlow(SampleCatalog.initialWatchHistory)
    val watchHistory: StateFlow<List<WatchHistoryItem>> = _watchHistory.asStateFlow()

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    // Navigation and player state
    private val _currentPlayingShow = MutableStateFlow<Show>(SampleCatalog.featuredShow)
    val currentPlayingShow: StateFlow<Show> = _currentPlayingShow.asStateFlow()

    private val _selectedSeasonNumber = MutableStateFlow(2)
    val selectedSeasonNumber: StateFlow<Int> = _selectedSeasonNumber.asStateFlow()

    private val _currentPlayingEpisode = MutableStateFlow<Episode>(
        SampleCatalog.featuredShow.seasons.find { it.seasonNumber == 2 }?.episodes?.firstOrNull()
            ?: SampleCatalog.featuredShow.seasons.first().episodes.first()
    )
    val currentPlayingEpisode: StateFlow<Episode> = _currentPlayingEpisode.asStateFlow()

    // Notifications state
    private val _unreadNotificationsCount = MutableStateFlow(2)
    val unreadNotificationsCount: StateFlow<Int> = _unreadNotificationsCount.asStateFlow()

    private var currentUserId = "user_alex_mercer"

    init {
        // Initial cloud sync with Firestore
        FirestoreService.syncCatalog(_catalog.value)
        FirestoreService.syncUserProfile(currentUserId, _profile.value)
        FirestoreService.syncMyList(currentUserId, _myListIds.value)
        FirestoreService.syncWatchHistory(currentUserId, _watchHistory.value)

        // Real-time snapshot listener from Firestore
        FirestoreService.listenToCloudSync(
            userId = currentUserId,
            onProfileUpdated = { remoteProfile ->
                _profile.value = remoteProfile
            },
            onMyListUpdated = { remoteList ->
                _myListIds.value = remoteList
            }
        )
    }

    fun syncUserWithCloud(uid: String, email: String, displayName: String) {
        currentUserId = uid
        _profile.update { it.copy(name = displayName, email = email) }
        FirestoreService.syncUserProfile(uid, _profile.value)
        FirestoreService.syncWatchHistory(uid, _watchHistory.value)
        FirestoreService.syncMyList(uid, _myListIds.value)
        FirestoreService.listenToCloudSync(
            userId = uid,
            onProfileUpdated = { remoteProfile ->
                _profile.value = remoteProfile
            },
            onMyListUpdated = { remoteList ->
                _myListIds.value = remoteList
            }
        )
    }

    fun updateUserProfile(displayName: String, photoUrl: String?) {
        _profile.update { it.copy(name = displayName, avatarUrl = photoUrl ?: it.avatarUrl) }
        FirestoreService.updateUserProfileDetails(currentUserId, displayName, photoUrl)
    }

    fun toggleMyList(showId: String) {
        _myListIds.update { current ->
            if (current.contains(showId)) current - showId else current + showId
        }
        FirestoreService.syncMyList(currentUserId, _myListIds.value)
    }

    fun isShowInMyList(showId: String): Boolean {
        return _myListIds.value.contains(showId)
    }

    fun selectShow(show: Show, seasonNum: Int? = null, episode: Episode? = null) {
        _currentPlayingShow.value = show
        val activeSeasonNum = seasonNum ?: show.seasons.firstOrNull()?.seasonNumber ?: 1
        _selectedSeasonNumber.value = activeSeasonNum
        val season = show.seasons.find { it.seasonNumber == activeSeasonNum } ?: show.seasons.firstOrNull()
        val activeEp = episode ?: season?.episodes?.firstOrNull()
        if (activeEp != null) {
            _currentPlayingEpisode.value = activeEp
        }

        // Add to watch history and sync with Firestore cloud
        val historyEntry = WatchHistoryItem(
            showId = show.id,
            showTitle = show.title,
            episodeTitle = activeEp?.let { "EP ${it.episodeNumber}: ${it.title}" } ?: "Watched",
            episodeNumber = activeEp?.episodeNumber ?: 1,
            progressFraction = 0.15f,
            lastWatchedText = "Just now",
            posterUrl = show.posterUrl
        )
        _watchHistory.update { current ->
            listOf(historyEntry) + current.filterNot { it.showId == show.id }
        }
        FirestoreService.syncWatchHistory(currentUserId, _watchHistory.value)
    }

    fun selectSeason(seasonNumber: Int) {
        _selectedSeasonNumber.value = seasonNumber
        val season = _currentPlayingShow.value.seasons.find { it.seasonNumber == seasonNumber }
        val firstEp = season?.episodes?.firstOrNull()
        if (firstEp != null) {
            _currentPlayingEpisode.value = firstEp
        }
    }

    fun selectEpisode(episode: Episode) {
        _currentPlayingEpisode.value = episode
    }

    fun startDownload(show: Show, episode: Episode) {
        val existing = _downloads.value.find { it.showId == show.id && it.episodeNumber == episode.episodeNumber }
        if (existing == null) {
            val newItem = DownloadItem(
                id = "dl_${System.currentTimeMillis()}",
                showId = show.id,
                showTitle = show.title,
                episodeNumber = episode.episodeNumber,
                episodeTitle = episode.title,
                thumbnailUrl = episode.thumbnailUrl,
                sizeMb = episode.downloadSizeMb,
                progress = 0.05f,
                status = DownloadStatus.DOWNLOADING,
                quality = "1080p Master"
            )
            _downloads.update { listOf(newItem) + it }
        }
    }

    fun toggleDownloadPause(downloadId: String) {
        _downloads.update { list ->
            list.map { item ->
                if (item.id == downloadId) {
                    val nextStatus = if (item.status == DownloadStatus.DOWNLOADING) DownloadStatus.PAUSED else DownloadStatus.DOWNLOADING
                    item.copy(status = nextStatus)
                } else item
            }
        }
    }

    fun completeDownload(downloadId: String) {
        _downloads.update { list ->
            list.map { item ->
                if (item.id == downloadId) item.copy(status = DownloadStatus.COMPLETED, progress = 1.0f) else item
            }
        }
    }

    fun deleteDownload(downloadId: String) {
        _downloads.update { list -> list.filterNot { it.id == downloadId } }
    }

    fun clearAllDownloads() {
        _downloads.value = emptyList()
    }

    fun toggleWifiOnly() {
        _profile.update { it.copy(wifiOnlyDownloads = !it.wifiOnlyDownloads) }
        FirestoreService.syncUserProfile(currentUserId, _profile.value)
    }

    fun toggleSmartDownloads() {
        _profile.update { it.copy(smartDownloads = !it.smartDownloads) }
        FirestoreService.syncUserProfile(currentUserId, _profile.value)
    }

    fun clearCache() {
        _profile.update { it.copy(cacheSizeMb = 12.4) }
    }

    fun clearNotifications() {
        _unreadNotificationsCount.value = 0
    }
}
