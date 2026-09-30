package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import com.example.data.SearchPreferencesRepository
import com.example.ui.components.AuthModalBottomSheet
import com.example.ui.components.AudioSubtitleBottomSheet
import com.example.ui.components.CastDeviceDialog
import com.example.ui.components.CreatorStudioBottomSheet
import com.example.ui.components.NotificationCenterSheet
import com.example.ui.components.SeasonSelectorBottomSheet
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlayerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonCyanSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(
    viewModel: StreamXViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.attachSearchPrefsRepo(SearchPreferencesRepository(context))
    }

    val currentTab by viewModel.currentTab.collectAsState()
    val isPlayerOpen by viewModel.isPlayerOpen.collectAsState()
    val catalog by viewModel.catalog.collectAsState()
    val myListIds by viewModel.myListIds.collectAsState()
    val downloads by viewModel.downloads.collectAsState()
    val watchHistory by viewModel.watchHistory.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val currentPlayingShow by viewModel.currentPlayingShow.collectAsState()
    val selectedSeasonNumber by viewModel.selectedSeasonNumber.collectAsState()
    val currentPlayingEpisode by viewModel.currentPlayingEpisode.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val unreadNotifications by viewModel.unreadNotifications.collectAsState()
    val isFirestoreLoading by viewModel.isFirestoreLoading.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSearchGenre by viewModel.selectedSearchGenre.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()

    val authUserState by viewModel.authUserState.collectAsState()
    val showAuthBottomSheet by viewModel.showAuthBottomSheet.collectAsState()

    val showSeasonSheet by viewModel.showSeasonBottomSheet.collectAsState()
    val showAudioSubtitleSheet by viewModel.showAudioSubtitleSheet.collectAsState()
    val showCreatorStudioSheet by viewModel.showCreatorStudioSheet.collectAsState()
    val showCastDialog by viewModel.showCastDialog.collectAsState()
    val showNotificationSheet by viewModel.showNotificationSheet.collectAsState()
    val selectedAudioTrack by viewModel.selectedAudioTrack.collectAsState()
    val selectedSubtitleTrack by viewModel.selectedSubtitleTrack.collectAsState()

    Scaffold(
        containerColor = DarkBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (!isPlayerOpen) {
                CustomBottomNavigationBar(
                    selectedTab = currentTab,
                    onTabSelected = { viewModel.setTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentTab) {
                NavigationTab.HOME -> {
                    HomeScreen(
                        featuredShow = catalog.firstOrNull() ?: currentPlayingShow,
                        catalog = catalog,
                        watchHistory = watchHistory,
                        myListIds = myListIds,
                        selectedCategory = selectedCategory,
                        unreadNotifications = unreadNotifications,
                        isLoading = isFirestoreLoading,
                        onCategorySelected = { viewModel.setCategory(it) },
                        onShowSelected = { show -> viewModel.openPlayer(show) },
                        onToggleMyList = { id -> viewModel.toggleMyList(id) },
                        onCastClicked = { viewModel.setCastDialog(true) },
                        onNotificationClicked = { viewModel.setNotificationSheet(true) },
                        contentPadding = innerPadding
                    )
                }

                NavigationTab.SEARCH -> {
                    SearchScreen(
                        searchQuery = searchQuery,
                        selectedGenre = selectedSearchGenre,
                        searchResults = searchResults,
                        recentSearches = recentSearches,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        onGenreSelected = { viewModel.setSearchGenre(it) },
                        onShowSelected = { show -> viewModel.openPlayer(show) },
                        onSaveRecentSearch = { q -> viewModel.addRecentSearch(q) },
                        onRemoveRecentSearch = { q -> viewModel.removeRecentSearch(q) },
                        onClearRecentSearches = { viewModel.clearAllRecentSearches() },
                        contentPadding = innerPadding
                    )
                }

                NavigationTab.OFFLINE -> {
                    DownloadsScreen(
                        downloads = downloads,
                        catalog = catalog,
                        smartDownloads = profile.smartDownloads,
                        onToggleSmartDownloads = { viewModel.toggleSmartDownloads() },
                        onTogglePause = { id -> viewModel.toggleDownloadPause(id) },
                        onDeleteDownload = { id -> viewModel.deleteDownload(id) },
                        onPlayDownload = { show, epNum ->
                            val ep = show.seasons.flatMap { it.episodes }.find { it.episodeNumber == epNum }
                            viewModel.openPlayer(show, seasonNumber = null, episode = ep)
                        },
                        contentPadding = innerPadding
                    )
                }

                NavigationTab.ME -> {
                    ProfileScreen(
                        profile = profile,
                        onToggleWifiOnly = { viewModel.toggleWifiOnly() },
                        onClearCache = { viewModel.clearCache() },
                        onOpenCreatorStudio = { viewModel.setCreatorStudioSheet(true) },
                        onOpenAuth = { viewModel.setAuthBottomSheet(true) },
                        onUpdateProfile = { name, avatarUrl -> viewModel.updateUserProfile(name, avatarUrl) },
                        contentPadding = innerPadding
                    )
                }
            }

            // FULLSCREEN PLAYER SCREEN OVERLAY
            AnimatedVisibility(
                visible = isPlayerOpen,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                PlayerScreen(
                    show = currentPlayingShow,
                    selectedSeasonNumber = selectedSeasonNumber,
                    currentEpisode = currentPlayingEpisode,
                    downloads = downloads,
                    isInMyList = myListIds.contains(currentPlayingShow.id),
                    onBack = { viewModel.closePlayer() },
                    onOpenSeasonSelector = { viewModel.setSeasonBottomSheet(true) },
                    onEpisodeSelected = { ep -> viewModel.selectEpisode(ep) },
                    onOpenAudioSubtitles = { viewModel.setAudioSubtitleSheet(true) },
                    onDownloadEpisode = { viewModel.downloadCurrentEpisode() },
                    onToggleMyList = { viewModel.toggleMyList(currentPlayingShow.id) },
                    onShowSelected = { show -> viewModel.openPlayer(show) },
                    recommendations = catalog
                )
            }
        }
    }

    // MODAL BOTTOM SHEETS
    if (showSeasonSheet) {
        SeasonSelectorBottomSheet(
            seasons = currentPlayingShow.seasons,
            selectedSeasonNumber = selectedSeasonNumber,
            onSeasonSelected = { seasonNum -> viewModel.selectSeason(seasonNum) },
            onDismiss = { viewModel.setSeasonBottomSheet(false) }
        )
    }

    if (showAudioSubtitleSheet) {
        AudioSubtitleBottomSheet(
            selectedAudio = selectedAudioTrack,
            selectedSubtitle = selectedSubtitleTrack,
            onAudioSelected = { viewModel.setAudioTrack(it) },
            onSubtitleSelected = { viewModel.setSubtitleTrack(it) },
            onDismiss = { viewModel.setAudioSubtitleSheet(false) }
        )
    }

    if (showCreatorStudioSheet) {
        CreatorStudioBottomSheet(
            onDismiss = { viewModel.setCreatorStudioSheet(false) }
        )
    }

    if (showCastDialog) {
        CastDeviceDialog(
            onDismiss = { viewModel.setCastDialog(false) }
        )
    }

    if (showNotificationSheet) {
        NotificationCenterSheet(
            onDismiss = { viewModel.setNotificationSheet(false) }
        )
    }

    if (showAuthBottomSheet) {
        AuthModalBottomSheet(
            currentUser = authUserState,
            onDismiss = { viewModel.setAuthBottomSheet(false) },
            onUserAuthenticated = { user ->
                viewModel.onUserAuthenticated(user)
            }
        )
    }
}

@Composable
fun CustomBottomNavigationBar(
    selectedTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val horizontalPadding = if (isLandscape) 120.dp else 16.dp
    val verticalPadding = if (isLandscape) 4.dp else 8.dp

    // Glassmorphism effects with Neon Cyan active accents
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .clip(RoundedCornerShape(24.dp))
            .background(DarkSurface.copy(alpha = 0.92f))
            .border(1.dp, DarkBorder, RoundedCornerShape(24.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavTabItem(
                title = "Home",
                selected = selectedTab == NavigationTab.HOME,
                filledIcon = Icons.Default.Home,
                outlinedIcon = Icons.Outlined.Home,
                onClick = { onTabSelected(NavigationTab.HOME) },
                testTag = "nav_tab_home"
            )

            BottomNavTabItem(
                title = "Search",
                selected = selectedTab == NavigationTab.SEARCH,
                filledIcon = Icons.Default.Search,
                outlinedIcon = Icons.Outlined.Search,
                onClick = { onTabSelected(NavigationTab.SEARCH) },
                testTag = "nav_tab_search"
            )

            BottomNavTabItem(
                title = "Offline",
                selected = selectedTab == NavigationTab.OFFLINE,
                filledIcon = Icons.Default.Download,
                outlinedIcon = Icons.Outlined.Download,
                onClick = { onTabSelected(NavigationTab.OFFLINE) },
                testTag = "nav_tab_offline"
            )

            BottomNavTabItem(
                title = "Me",
                selected = selectedTab == NavigationTab.ME,
                filledIcon = Icons.Default.Person,
                outlinedIcon = Icons.Outlined.Person,
                onClick = { onTabSelected(NavigationTab.ME) },
                testTag = "nav_tab_me"
            )
        }
    }
}

@Composable
fun BottomNavTabItem(
    title: String,
    selected: Boolean,
    filledIcon: ImageVector,
    outlinedIcon: ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) NeonCyanSubtle else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (selected) filledIcon else outlinedIcon,
                contentDescription = title,
                tint = if (selected) NeonCyan else TextMuted,
                modifier = Modifier
                    .size(22.dp)
                    .then(
                        if (selected) {
                            Modifier.drawBehind {
                                drawCircle(
                                    color = NeonCyanGlow,
                                    radius = size.maxDimension * 0.7f,
                                    center = center
                                )
                            }
                        } else Modifier
                    )
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = title,
                color = if (selected) NeonCyan else TextMuted,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                letterSpacing = 0.3.sp
            )
        }
    }
}
