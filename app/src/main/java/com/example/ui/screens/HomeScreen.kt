package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Show
import com.example.data.WatchHistoryItem
import com.example.ui.components.MatchScoreBadge
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.components.QualityPill
import com.example.ui.components.RatingBadge
import com.example.ui.components.ShimmerHomeScreenSkeleton
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonCyanSubtle
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    featuredShow: Show,
    catalog: List<Show>,
    watchHistory: List<WatchHistoryItem>,
    myListIds: Set<String>,
    selectedCategory: String,
    unreadNotifications: Int,
    isLoading: Boolean = false,
    onCategorySelected: (String) -> Unit,
    onShowSelected: (Show) -> Unit,
    onToggleMyList: (String) -> Unit,
    onCastClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    contentPadding: PaddingValues
) {
    val categories = listOf("All", "Kdrama", "Anime", "Movies", "Trending", "Originals")
    var feedbackToast by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(feedbackToast) {
        if (feedbackToast != null) {
            kotlinx.coroutines.delay(2200)
            feedbackToast = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        Crossfade(
            targetState = isLoading,
            label = "home_shimmer_crossfade"
        ) { loading ->
            if (loading) {
                ShimmerHomeScreenSkeleton(contentPadding = contentPadding)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 0.dp,
                        bottom = contentPadding.calculateBottomPadding() + 24.dp
                    ),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Hero section with banner & play buttons
                    item {
                        FeaturedHeroBanner(
                            show = featuredShow,
                            isInMyList = myListIds.contains(featuredShow.id),
                            onPlay = { onShowSelected(featuredShow) },
                            onToggleMyList = {
                                val willBeInList = !myListIds.contains(featuredShow.id)
                                onToggleMyList(featuredShow.id)
                                feedbackToast = if (willBeInList) "Added to My List • Saved to Cloud" else "Removed from My List • Saved to Cloud"
                            },
                            onDetails = { onShowSelected(featuredShow) }
                        )
                    }

                    // Continue Watching horizontal rail
                    if (watchHistory.isNotEmpty()) {
                        item {
                            SectionHeader(title = "CONTINUE WATCHING", subtitle = "Resume from last stream")
                            Spacer(modifier = Modifier.height(10.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(watchHistory) { historyItem ->
                                    val matchedShow = catalog.find { it.id == historyItem.showId } ?: featuredShow
                                    ContinueWatchingCard(
                                        item = historyItem,
                                        onClick = { onShowSelected(matchedShow) }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    // Trending Now horizontal rail with Top 10 badges
                    item {
                        SectionHeader(title = "TRENDING NOW", subtitle = "Top ranked streams today")
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(catalog) { index, show ->
                                ShowCard(
                                    show = show,
                                    rankNumber = if (show.isTop10) show.top10Rank else (index + 1),
                                    onClick = { onShowSelected(show) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Suggested For You
                    item {
                        SectionHeader(title = "SUGGESTED FOR YOU", subtitle = "Curated based on 4K HDR playback")
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(catalog.reversed()) { show ->
                                CompactShowCard(
                                    show = show,
                                    onClick = { onShowSelected(show) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Top Kdrama & Anime Spotlight
                    item {
                        SectionHeader(title = "TOP KDRAMA & ANIME SPOTLIGHT", subtitle = "Exclusive releases & sub/dub master")
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(catalog.filter { it.category in listOf("Kdrama", "Anime") }) { show ->
                                ShowCard(
                                    show = show,
                                    rankNumber = null,
                                    onClick = { onShowSelected(show) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Top Transparent Overlay App Bar with StreamX Logo and Actions
        TopDiscoveryBar(
            selectedCategory = selectedCategory,
            categories = categories,
            unreadNotifications = unreadNotifications,
            onCategorySelected = onCategorySelected,
            onCastClicked = onCastClicked,
            onNotificationClicked = onNotificationClicked
        )

        // Floating Firestore My List Feedback Pill
        feedbackToast?.let { msg ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = contentPadding.calculateBottomPadding() + 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF001B20))
                    .border(1.2.dp, NeonCyan, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = msg,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TopDiscoveryBar(
    selectedCategory: String,
    categories: List<String>,
    unreadNotifications: Int,
    onCategorySelected: (String) -> Unit,
    onCastClicked: () -> Unit,
    onNotificationClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBg.copy(alpha = 0.95f),
                        DarkBg.copy(alpha = 0.75f),
                        Color.Transparent
                    )
                )
            )
            .statusBarsPadding()
            .padding(top = 8.dp, bottom = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Neon StreamX Branding
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(listOf(NeonCyan, NeonPurple))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Logo",
                        tint = Color(0xFF0A0A0F),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "STREAM",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "X",
                    color = NeonCyan,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            // Top action icons: Cast and Notification
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onCastClicked,
                    modifier = Modifier.testTag("cast_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Cast,
                        contentDescription = "Cast",
                        tint = TextPrimary
                    )
                }

                IconButton(
                    onClick = onNotificationClicked,
                    modifier = Modifier.testTag("notification_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifications > 0) {
                                Badge(
                                    containerColor = NeonCyan,
                                    contentColor = Color(0xFF001B20)
                                ) {
                                    Text(text = "$unreadNotifications")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal Category Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = category.equals(selectedCategory, ignoreCase = true)
                val bgColor = if (isSelected) NeonCyan else DarkSurface.copy(alpha = 0.8f)
                val textColor = if (isSelected) Color(0xFF001B20) else TextPrimary
                val borderColor = if (isSelected) NeonCyan else DarkBorder

                Box(
                    modifier = Modifier
                        .testTag("category_pill_$category")
                        .clip(RoundedCornerShape(20.dp))
                        .background(bgColor)
                        .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = category,
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedHeroBanner(
    show: Show,
    isInMyList: Boolean,
    onPlay: () -> Unit,
    onToggleMyList: () -> Unit,
    onDetails: () -> Unit
) {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val bannerHeight = if (isLandscape) 360.dp else 490.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(bannerHeight)
    ) {
        // High-res banner image with gradient fading into background
        AsyncImage(
            model = show.bannerUrl,
            contentDescription = show.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient layers for readability and seamless OLED transition
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            DarkBg.copy(alpha = 0.3f),
                            DarkBg.copy(alpha = 0.85f),
                            DarkBg
                        ),
                        startY = 150f
                    )
                )
        )

        // Hero Content overlay at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Badges row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonPink.copy(alpha = 0.2f))
                        .border(1.dp, NeonPink, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "TOP 10 TODAY #1",
                        color = NeonPink,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    )
                }

                MatchScoreBadge(score = show.matchScore)
                RatingBadge(rating = show.ratingScore)
                QualityPill(text = show.qualityTag, isNeon = true)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Show Title
            Text(
                text = show.title,
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )

            if (show.japaneseOrKoreanTitle.isNotEmpty()) {
                Text(
                    text = show.japaneseOrKoreanTitle,
                    color = NeonCyan.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Genres list
            Text(
                text = show.genres.joinToString(" • "),
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Row: Play, My List, Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeonPrimaryButton(
                    text = "PLAY NOW",
                    icon = Icons.Default.PlayArrow,
                    onClick = onPlay,
                    modifier = Modifier.weight(1.3f),
                    testTag = "hero_play_button"
                )

                // My List Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_my_list_button")
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, if (isInMyList) NeonCyan else DarkBorder, RoundedCornerShape(12.dp))
                        .clickable(onClick = onToggleMyList)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isInMyList) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            tint = if (isInMyList) NeonCyan else TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isInMyList) "IN LIST" else "MY LIST",
                            color = if (isInMyList) NeonCyan else TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Info Button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .clickable(onClick = onDetails),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Details",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 16.dp)
                    .background(NeonCyan, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.8.sp
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(start = 11.dp)
        )
    }
}

@Composable
fun ShowCard(
    show: Show,
    rankNumber: Int?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                AsyncImage(
                    model = show.posterUrl,
                    contentDescription = show.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Quality Badge overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {
                    QualityPill(text = "4K", isNeon = true)
                }

                // Rank badge if present
                if (rankNumber != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                            .size(26.dp)
                            .background(DarkBg.copy(alpha = 0.9f), CircleShape)
                            .border(1.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "#$rankNumber",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = show.title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = show.category,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "★ ${show.ratingScore}",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CompactShowCard(show: Show, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                AsyncImage(
                    model = show.bannerUrl,
                    contentDescription = show.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(34.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = show.title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${show.matchScore}% Match • ${show.year}",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun ContinueWatchingCard(item: WatchHistoryItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
            ) {
                AsyncImage(
                    model = item.posterUrl,
                    contentDescription = item.showTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Semi-transparent center play badge
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                        .border(1.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Resume",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Progress Bar overlay at the bottom of the thumbnail
                LinearProgressIndicator(
                    progress = { item.progressFraction },
                    color = NeonCyan,
                    trackColor = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.BottomCenter)
                )
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = item.showTitle,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.episodeTitle,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    }
}
