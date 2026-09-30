package com.example.ui.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.Build
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.data.DownloadItem
import com.example.data.DownloadStatus
import com.example.data.Episode
import com.example.data.Season
import com.example.data.Show
import com.example.ui.components.QualityPill
import com.example.ui.components.RatingBadge
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonCyanSubtle
import com.example.ui.theme.TagGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    show: Show,
    selectedSeasonNumber: Int,
    currentEpisode: Episode,
    downloads: List<DownloadItem>,
    isInMyList: Boolean,
    onBack: () -> Unit,
    onOpenSeasonSelector: () -> Unit,
    onEpisodeSelected: (Episode) -> Unit,
    onOpenAudioSubtitles: () -> Unit,
    onDownloadEpisode: () -> Unit,
    onToggleMyList: () -> Unit,
    onShowSelected: (Show) -> Unit,
    recommendations: List<Show>
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    BackHandler {
        if (isLandscape) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            onBack()
        }
    }

    val currentSeason = show.seasons.find { it.seasonNumber == selectedSeasonNumber } ?: show.seasons.first()

    val currentEpIndex = currentSeason.episodes.indexOfFirst { it.id == currentEpisode.id }
    val onNextEpisode: (() -> Unit)? = if (currentEpIndex != -1 && currentEpIndex < currentSeason.episodes.size - 1) {
        { onEpisodeSelected(currentSeason.episodes[currentEpIndex + 1]) }
    } else null
    val onPrevEpisode: (() -> Unit)? = if (currentEpIndex > 0) {
        { onEpisodeSelected(currentSeason.episodes[currentEpIndex - 1]) }
    } else null

    // Download state for this episode
    val isDownloaded = downloads.any {
        it.showId == show.id && it.episodeNumber == currentEpisode.episodeNumber && it.status == DownloadStatus.COMPLETED
    }
    val isDownloading = downloads.any {
        it.showId == show.id && it.episodeNumber == currentEpisode.episodeNumber && it.status == DownloadStatus.DOWNLOADING
    }

    if (isLandscape) {
        // FULLSCREEN CINEMA THEATER MODE IN LANDSCAPE
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            NativeVideoPlayerBox(
                videoUrl = currentEpisode.videoUrl,
                title = show.title,
                episodeTitle = currentEpisode.title,
                backdropUrl = currentEpisode.thumbnailUrl.ifEmpty { show.bannerUrl },
                onBack = {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                },
                onNextEpisode = onNextEpisode,
                onPreviousEpisode = onPrevEpisode,
                onOpenAudioSubtitles = onOpenAudioSubtitles,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg)
        ) {
            // TOP NATIVE VIDEO PLAYER SECTION
            item {
                NativeVideoPlayerBox(
                    videoUrl = currentEpisode.videoUrl,
                    title = show.title,
                    episodeTitle = currentEpisode.title,
                    backdropUrl = currentEpisode.thumbnailUrl.ifEmpty { show.bannerUrl },
                    onBack = onBack,
                    onNextEpisode = onNextEpisode,
                    onPreviousEpisode = onPrevEpisode,
                    onOpenAudioSubtitles = onOpenAudioSubtitles,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            }

        // METADATA PILLS & DETAILS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Show Title
                Text(
                    text = show.title,
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.3).sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Episode Number & Name
                Text(
                    text = "EP ${currentEpisode.episodeNumber}: ${currentEpisode.title} (${currentEpisode.durationText})",
                    color = NeonCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // ROW OF METADATA PILLS: ["ANIME", "Season X", "⭐ 9.9", "1080P MASTER"]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Category Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = show.category.uppercase(),
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // SEASON SELECTOR PILL (Wrapped in GestureDetector / Clickable)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .testTag("season_selector_pill")
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonCyanSubtle)
                            .border(1.2.dp, NeonCyan, RoundedCornerShape(8.dp))
                            .clickable(onClick = onOpenSeasonSelector)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Season $selectedSeasonNumber",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Season",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Rating Pill
                    RatingBadge(rating = show.ratingScore)

                    // Master Quality Pill
                    QualityPill(text = show.qualityTag, isNeon = true)

                    // Audio Badge
                    QualityPill(text = show.audioBadge, isNeon = false)
                }

                Spacer(modifier = Modifier.height(18.dp))

                // EXTRA PLAYER FEATURES BUTTONS:
                // "Audio / Subtitles", "Download Episode", "My List", "Share"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Download Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("download_episode_button")
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(
                                1.dp,
                                if (isDownloaded) TagGreen else DarkBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable(onClick = onDownloadEpisode)
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isDownloading) {
                                CircularProgressIndicator(
                                    color = NeonCyan,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = if (isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                    contentDescription = null,
                                    tint = if (isDownloaded) TagGreen else NeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when {
                                    isDownloaded -> "DOWNLOADED"
                                    isDownloading -> "DOWNLOADING"
                                    else -> "DOWNLOAD"
                                },
                                color = if (isDownloaded) TagGreen else TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Audio / Subtitles Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("audio_subtitles_button")
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                            .clickable(onClick = onOpenAudioSubtitles)
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Subtitles,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AUDIO / SUBS",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // My List toggle
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, if (isInMyList) NeonCyan else DarkBorder, RoundedCornerShape(12.dp))
                            .clickable(onClick = onToggleMyList),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isInMyList) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = "My List",
                            tint = if (isInMyList) NeonCyan else TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Summary / Synopsis
                Text(
                    text = currentEpisode.summary.ifEmpty { show.description },
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        // EPISODE SELECTOR: Horizontal list of square/rounded episode buttons
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EPISODES (${currentSeason.episodes.size})",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Season $selectedSeasonNumber",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = onOpenSeasonSelector)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Horizontal Episode Square Chips: E1, E2, E3...
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(currentSeason.episodes) { ep ->
                        val isPlaying = ep.id == currentEpisode.id
                        EpisodeSquareChip(
                            episodeNumber = ep.episodeNumber,
                            isPlaying = isPlaying,
                            onClick = { onEpisodeSelected(ep) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Active Episode Info Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Episode ${currentEpisode.episodeNumber}: ${currentEpisode.title}",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = currentEpisode.durationText,
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        if (currentEpisode.summary.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentEpisode.summary,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }

        // MORE LIKE THIS RECOMMENDATIONS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp)
            ) {
                Text(
                    text = "MORE LIKE THIS",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recommendations.filter { it.id != show.id }) { rec ->
                        Box(
                            modifier = Modifier
                                .width(130.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceElevated)
                                .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
                                .clickable { onShowSelected(rec) }
                        ) {
                            Column {
                                AsyncImage(
                                    model = rec.posterUrl,
                                    contentDescription = rec.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(170.dp)
                                )
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = rec.title,
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${rec.matchScore}% Match",
                                        color = TagGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
fun EpisodeSquareChip(
    episodeNumber: Int,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isPlaying) NeonCyan else DarkBorder
    val borderWidth = if (isPlaying) 1.5.dp else 1.dp
    val bgColor = if (isPlaying) NeonCyan.copy(alpha = 0.2f) else DarkSurfaceElevated

    Box(
        modifier = Modifier
            .size(52.dp)
            .testTag("episode_square_$episodeNumber")
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(borderWidth, borderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "E$episodeNumber",
                color = if (isPlaying) NeonCyan else TextPrimary,
                fontSize = 15.sp,
                fontWeight = if (isPlaying) FontWeight.Black else FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            if (isPlaying) {
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(NeonCyan, CircleShape)
                )
            }
        }
    }
}

enum class PlayerAspectRatio(val label: String, val description: String) {
    FIT("FIT", "16:9 Letterbox"),
    STRETCH("STRETCH", "Fullscreen Stretch"),
    CROP("CROP", "Zoom & Crop Center"),
    CINEMA_21_9("21:9", "Ultra-Wide Cinema")
}

@Composable
fun NativeVideoPlayerBox(
    videoUrl: String,
    title: String,
    episodeTitle: String,
    backdropUrl: String,
    onBack: () -> Unit,
    onNextEpisode: (() -> Unit)? = null,
    onPreviousEpisode: (() -> Unit)? = null,
    onOpenAudioSubtitles: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var isPlaying by remember { mutableStateOf(true) }
    var isPlayerPrepared by remember { mutableStateOf(false) }
    var isPlayerError by remember { mutableStateOf(false) }
    var currentPositionMs by remember { mutableIntStateOf(0) }
    var durationMs by remember { mutableIntStateOf(60000) } // ~60s test video
    var showControls by remember { mutableStateOf(true) }
    var showSkipIntroButton by remember { mutableStateOf(false) }
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    var mediaPlayerRef by remember { mutableStateOf<MediaPlayer?>(null) }

    // Pro player features
    var isBuffering by remember { mutableStateOf(false) }
    var isScreenLocked by remember { mutableStateOf(false) }
    var aspectRatioMode by remember { mutableStateOf(PlayerAspectRatio.FIT) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var seekFeedbackText by remember { mutableStateOf<String?>(null) }
    var feedbackToastText by remember { mutableStateOf<String?>(null) }

    // Auto-dismiss buffer spinner
    LaunchedEffect(isBuffering) {
        if (isBuffering) {
            delay(900)
            isBuffering = false
        }
    }

    // Dismiss seek indicator
    LaunchedEffect(seekFeedbackText) {
        if (seekFeedbackText != null) {
            delay(850)
            seekFeedbackText = null
        }
    }

    // Dismiss toast indicator
    LaunchedEffect(feedbackToastText) {
        if (feedbackToastText != null) {
            delay(2000)
            feedbackToastText = null
        }
    }

    // Safe lifecycle management per video URL
    DisposableEffect(videoUrl) {
        isPlayerPrepared = false
        isPlayerError = false
        onDispose {
            try {
                videoViewRef?.stopPlayback()
            } catch (_: Throwable) {}
            videoViewRef = null
            mediaPlayerRef = null
        }
    }

    // Auto-hide controls timer
    LaunchedEffect(showControls, isPlaying, isScreenLocked) {
        if (showControls && isPlaying && !isScreenLocked) {
            delay(4200)
            showControls = false
        }
    }

    // Live playback ticker supporting playbackSpeed
    LaunchedEffect(isPlaying, isPlayerPrepared, isPlayerError, playbackSpeed) {
        while (isPlaying) {
            delay((1000 / playbackSpeed).toLong().coerceAtLeast(300))
            if (isPlayerPrepared && !isPlayerError && videoViewRef != null) {
                try {
                    val cur = videoViewRef?.currentPosition ?: (currentPositionMs + (1000 * playbackSpeed).toInt())
                    currentPositionMs = cur
                    val dur = videoViewRef?.duration ?: 0
                    if (dur > 0) durationMs = dur
                } catch (_: Throwable) {
                    currentPositionMs = (currentPositionMs + (1000 * playbackSpeed).toInt()).coerceAtMost(durationMs)
                }
            } else {
                currentPositionMs = (currentPositionMs + (1000 * playbackSpeed).toInt()).coerceAtMost(durationMs)
            }
            showSkipIntroButton = currentPositionMs in 0..90000
        }
    }

    // Calculate scale multipliers for aspect ratio modes
    val (scaleX, scaleY) = when (aspectRatioMode) {
        PlayerAspectRatio.FIT -> 1f to 1f
        PlayerAspectRatio.STRETCH -> if (isLandscape) (1.25f to 1.15f) else (1.3f to 1.25f)
        PlayerAspectRatio.CROP -> 1.38f to 1.38f
        PlayerAspectRatio.CINEMA_21_9 -> 1.32f to 0.95f
    }

    Box(
        modifier = modifier
            .background(Color.Black)
            .pointerInput(isScreenLocked, durationMs) {
                detectTapGestures(
                    onTap = {
                        if (isScreenLocked) {
                            showControls = true
                        } else {
                            showControls = !showControls
                        }
                    },
                    onDoubleTap = { offset ->
                        if (!isScreenLocked) {
                            val isLeft = offset.x < size.width * 0.42f
                            val isRight = offset.x > size.width * 0.58f
                            if (isLeft) {
                                val target = (currentPositionMs - 10000).coerceAtLeast(0)
                                currentPositionMs = target
                                try { videoViewRef?.seekTo(target) } catch (_: Throwable) {}
                                seekFeedbackText = "-10s"
                            } else if (isRight) {
                                val target = (currentPositionMs + 10000).coerceAtMost(durationMs)
                                currentPositionMs = target
                                try { videoViewRef?.seekTo(target) } catch (_: Throwable) {}
                                seekFeedbackText = "+10s"
                            }
                        }
                    }
                )
            }
    ) {
        // Video View layer clipped and scaled according to selected aspect ratio
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scaleX, scaleY),
                contentAlignment = Alignment.Center
            ) {
                // High-res backdrop layer
                AsyncImage(
                    model = backdropUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Native Android VideoView layer
                if (!isPlayerError) {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setOnErrorListener { _, what, extra ->
                                    android.util.Log.w("PlayerScreen", "MediaPlayer error: what=$what extra=$extra")
                                    isPlayerError = true
                                    isPlayerPrepared = false
                                    true
                                }

                                setOnPreparedListener { mp ->
                                    try {
                                        mediaPlayerRef = mp
                                        mp.isLooping = true
                                        val dur = mp.duration
                                        if (dur > 0) durationMs = dur
                                        isPlayerPrepared = true
                                        isPlayerError = false
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            try {
                                                mp.playbackParams = mp.playbackParams.setSpeed(playbackSpeed)
                                            } catch (_: Throwable) {}
                                        }
                                        start()
                                        isPlaying = true
                                    } catch (e: Throwable) {
                                        isPlayerError = true
                                    }
                                }

                                setOnInfoListener { _, what, _ ->
                                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                                        isBuffering = true
                                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                                        isBuffering = false
                                    }
                                    true
                                }

                                try {
                                    val uri = if (videoUrl.isNotBlank()) {
                                        Uri.parse(videoUrl)
                                    } else {
                                        Uri.parse("android.resource://${ctx.packageName}/raw/sample_stream")
                                    }
                                    setVideoURI(uri)
                                } catch (e: Throwable) {
                                    isPlayerError = true
                                }
                                videoViewRef = this
                            }
                        },
                        update = { view ->
                            videoViewRef = view
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .then(if (isPlayerPrepared) Modifier else Modifier.size(0.dp))
                    )
                }

                // Clean background while video initializes (No dummy text or fake watermark)
                if (!isPlayerPrepared && !isPlayerError) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = NeonCyan,
                            strokeWidth = 2.5.dp
                        )
                    }
                }
            }
        }

        // Center Buffering Spinner (Pure icon spinner, zero text)
        if (isBuffering) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .background(Color.Black.copy(alpha = 0.72f), CircleShape)
                    .border(1.5.dp, NeonCyan.copy(alpha = 0.85f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = NeonCyan,
                    strokeWidth = 3.dp
                )
            }
        }

        // Center Double-Tap Seek Feedback Overlay (+10s / -10s)
        seekFeedbackText?.let { seekText ->
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .border(1.5.dp, NeonCyan, RoundedCornerShape(32.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (seekText.startsWith("+")) Icons.Default.FastForward else Icons.Default.FastRewind,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = seekText,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // Floating Toast Indicator (Aspect Ratio, Speed, Orientation)
        feedbackToastText?.let { toast ->
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF001B20).copy(alpha = 0.9f))
                    .border(1.dp, NeonCyan, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 7.dp)
            ) {
                Text(
                    text = toast,
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Locked Screen Indicator & Floating Unlock Button
        if (isScreenLocked) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.8f))
                    .border(1.5.dp, NeonCyan, RoundedCornerShape(24.dp))
                    .clickable {
                        isScreenLocked = false
                        showControls = true
                        feedbackToastText = "Controls Unlocked"
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("unlock_screen_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = "Unlock",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SCREEN LOCKED • Tap to unlock",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Full Controls Overlay (Hidden when locked)
        AnimatedVisibility(
            visible = showControls && !isScreenLocked,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.62f))
            ) {
                // TOP BAR: Back, Title, Lock, Aspect Ratio, Speed, Subtitles, Fullscreen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }

                        Column {
                            Text(
                                text = title,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = episodeTitle,
                                color = NeonCyan,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }

                    // Action buttons row: Lock, Buffer, Aspect Ratio, Speed, Subtitles
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Lock Controls Button
                        IconButton(
                            onClick = {
                                isScreenLocked = true
                                showControls = false
                                feedbackToastText = "Screen Locked"
                            },
                            modifier = Modifier
                                .size(30.dp)
                                .testTag("lock_screen_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Screen",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Buffer Stream Icon Button (Pure icon, zero text)
                        IconButton(
                            onClick = {
                                isBuffering = true
                                try {
                                    val target = (currentPositionMs + 1000).coerceAtMost(durationMs)
                                    videoViewRef?.seekTo(target)
                                } catch (_: Throwable) {}
                            },
                            modifier = Modifier
                                .size(30.dp)
                                .testTag("buffer_stream_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Buffer Stream",
                                tint = if (isBuffering) NeonCyan else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Aspect Ratio Cycle Button: FIT -> STRETCH -> CROP -> 21:9
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceElevated)
                                .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .clickable {
                                    val nextMode = when (aspectRatioMode) {
                                        PlayerAspectRatio.FIT -> PlayerAspectRatio.STRETCH
                                        PlayerAspectRatio.STRETCH -> PlayerAspectRatio.CROP
                                        PlayerAspectRatio.CROP -> PlayerAspectRatio.CINEMA_21_9
                                        PlayerAspectRatio.CINEMA_21_9 -> PlayerAspectRatio.FIT
                                    }
                                    aspectRatioMode = nextMode
                                    feedbackToastText = "Aspect Ratio: ${nextMode.label}"
                                }
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                            .testTag("aspect_ratio_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AspectRatio,
                                    contentDescription = "Aspect Ratio",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = aspectRatioMode.label,
                                    color = TextPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Playback Speed Button: Cycles 0.75x -> 1.0x -> 1.25x -> 1.5x -> 2.0x -> 0.5x
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceElevated)
                                .border(1.dp, DarkBorder, RoundedCornerShape(6.dp))
                                .clickable {
                                    val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f, 0.5f)
                                    val curIndex = speeds.indexOfFirst { kotlin.math.abs(it - playbackSpeed) < 0.05f }
                                    val nextSpeed = speeds[(curIndex + 1).coerceAtLeast(0) % speeds.size]
                                    playbackSpeed = nextSpeed
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        try {
                                            mediaPlayerRef?.playbackParams = mediaPlayerRef?.playbackParams?.setSpeed(nextSpeed)
                                                ?: PlaybackParams().setSpeed(nextSpeed)
                                        } catch (_: Throwable) {}
                                    }
                                    feedbackToastText = "${nextSpeed}x"
                                }
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                            .testTag("playback_speed_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "Speed",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${playbackSpeed}x",
                                    color = TextPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Subtitle / Audio Button
                        onOpenAudioSubtitles?.let {
                            IconButton(
                                onClick = it,
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Subtitles,
                                    contentDescription = "Audio & Subtitles",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // CENTER CONTROLS: Prev Episode, -10s, Play/Pause, +10s, Next Episode (Clean, compact, clear look)
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(if (isLandscape) 28.dp else 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Episode
                    IconButton(
                        onClick = { onPreviousEpisode?.invoke() },
                        enabled = onPreviousEpisode != null,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous Episode",
                            tint = if (onPreviousEpisode != null) TextPrimary else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Rewind 10s
                    IconButton(
                        onClick = {
                            val target = (currentPositionMs - 10000).coerceAtLeast(0)
                            currentPositionMs = target
                            try { videoViewRef?.seekTo(target) } catch (_: Throwable) {}
                            seekFeedbackText = "-10s"
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastRewind,
                            contentDescription = "Rewind 10s",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Play/Pause button (Sleek translucent glass button, no bulky obscuring glow)
                    IconButton(
                        onClick = {
                            if (isPlaying) {
                                isPlaying = false
                                try { videoViewRef?.pause() } catch (_: Throwable) {}
                            } else {
                                isPlaying = true
                                try { videoViewRef?.start() } catch (_: Throwable) {}
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                            .border(1.5.dp, NeonCyan, CircleShape)
                            .testTag("play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = NeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Forward 10s
                    IconButton(
                        onClick = {
                            val target = (currentPositionMs + 10000).coerceAtMost(durationMs)
                            currentPositionMs = target
                            try { videoViewRef?.seekTo(target) } catch (_: Throwable) {}
                            seekFeedbackText = "+10s"
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Forward 10s",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Next Episode
                    IconButton(
                        onClick = { onNextEpisode?.invoke() },
                        enabled = onNextEpisode != null,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Episode",
                            tint = if (onNextEpisode != null) TextPrimary else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // BOTTOM CONTROLS: Slider, Timers, Clean Fullscreen button
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = if (isLandscape) 10.dp else 6.dp)
                ) {
                    val progressFraction = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs.toFloat()) else 0f

                    Slider(
                        value = progressFraction.coerceIn(0f, 1f),
                        onValueChange = { frac ->
                            val target = (frac * durationMs).toInt()
                            currentPositionMs = target
                            try { videoViewRef?.seekTo(target) } catch (_: Throwable) {}
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .testTag("video_progress_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formatTime(currentPositionMs),
                                color = NeonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = " / ${formatTime(durationMs)}",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }

                        // Compact Dedicated Landscape/Fullscreen Icon Button (Pure icon, clean look)
                        IconButton(
                            onClick = {
                                if (isLandscape) {
                                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                    feedbackToastText = "Portrait Mode"
                                } else {
                                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                                    feedbackToastText = "Fullscreen Cinema"
                                }
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("landscape_bottom_button")
                        ) {
                            Icon(
                                imageVector = if (isLandscape) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                contentDescription = if (isLandscape) "Exit Landscape" else "Landscape Mode",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Floating "SKIP INTRO" Button
        if (showSkipIntroButton && !isScreenLocked) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 48.dp)
                    .testTag("skip_intro_button")
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF001B20).copy(alpha = 0.9f))
                    .border(1.2.dp, NeonCyan, RoundedCornerShape(8.dp))
                    .clickable {
                        val target = (currentPositionMs + 85000).coerceAtMost(durationMs)
                        currentPositionMs = target
                        try { videoViewRef?.seekTo(target) } catch (_: Throwable) {}
                        showSkipIntroButton = false
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "SKIP INTRO +85s",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

private fun formatTime(millis: Int): String {
    val totalSeconds = (millis / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
