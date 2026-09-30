package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonCyan

/**
 * Global Shimmer Modifier for StreamX.
 * Sweeps a subtle, hyper-premium dark-slate & cyan tint gradient across the component.
 */
fun Modifier.shimmerEffect(
    durationMillis: Int = 1300,
    colors: List<Color> = listOf(
        Color(0xFF13131F),
        Color(0xFF222238),
        Color(0x2800F0FF),
        Color(0xFF222238),
        Color(0xFF13131F)
    )
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "streamx_shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(x = translateAnim - 300f, y = translateAnim - 300f),
        end = Offset(x = translateAnim + 300f, y = translateAnim + 300f)
    )

    background(brush)
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .border(1.dp, DarkBorder, shape)
            .shimmerEffect()
    )
}

/**
 * Shimmer skeleton for the Hero Banner section.
 */
@Composable
fun ShimmerHeroBannerSkeleton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(490.dp)
            .background(DarkBg)
    ) {
        // Shimmering background image placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )

        // Gradient overlay to mimic hero banner darkening
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            DarkBg.copy(alpha = 0.4f),
                            DarkBg.copy(alpha = 0.85f),
                            DarkBg
                        ),
                        startY = 150f
                    )
                )
        )

        // Skeleton overlay content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Badges row skeleton
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerBox(modifier = Modifier.size(width = 100.dp, height = 24.dp), shape = RoundedCornerShape(6.dp))
                ShimmerBox(modifier = Modifier.size(width = 80.dp, height = 24.dp), shape = RoundedCornerShape(6.dp))
                ShimmerBox(modifier = Modifier.size(width = 50.dp, height = 24.dp), shape = RoundedCornerShape(6.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title skeleton
            ShimmerBox(modifier = Modifier.size(width = 240.dp, height = 32.dp), shape = RoundedCornerShape(6.dp))
            Spacer(modifier = Modifier.height(6.dp))
            ShimmerBox(modifier = Modifier.size(width = 160.dp, height = 18.dp), shape = RoundedCornerShape(4.dp))

            Spacer(modifier = Modifier.height(10.dp))

            // Genres row skeleton
            ShimmerBox(modifier = Modifier.size(width = 200.dp, height = 14.dp), shape = RoundedCornerShape(4.dp))

            Spacer(modifier = Modifier.height(18.dp))

            // Action buttons skeleton: Play button & My List button
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ShimmerBox(
                    modifier = Modifier
                        .width(160.dp)
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                ShimmerBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

/**
 * Shimmer card skeleton for vertical posters in horizontal rails.
 */
@Composable
fun ShimmerRailCardSkeleton(
    cardWidth: Dp = 140.dp,
    cardHeight: Dp = 190.dp
) {
    Box(
        modifier = Modifier
            .width(cardWidth)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
    ) {
        Column {
            // Poster thumbnail shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .shimmerEffect()
            )

            // Info rows shimmer
            Column(modifier = Modifier.padding(10.dp)) {
                ShimmerBox(modifier = Modifier.fillMaxWidth(0.85f).height(14.dp), shape = RoundedCornerShape(4.dp))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ShimmerBox(modifier = Modifier.width(45.dp).height(11.dp), shape = RoundedCornerShape(3.dp))
                    ShimmerBox(modifier = Modifier.width(35.dp).height(11.dp), shape = RoundedCornerShape(3.dp))
                }
            }
        }
    }
}

/**
 * Shimmer card skeleton for horizontal 'Continue Watching' cards.
 */
@Composable
fun ShimmerContinueWatchingCardSkeleton() {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .shimmerEffect()
            )
            Column(modifier = Modifier.padding(10.dp)) {
                ShimmerBox(modifier = Modifier.fillMaxWidth(0.9f).height(14.dp), shape = RoundedCornerShape(4.dp))
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBox(modifier = Modifier.fillMaxWidth(0.6f).height(11.dp), shape = RoundedCornerShape(3.dp))
            }
        }
    }
}

/**
 * Full-screen Home shimmer skeleton displaying while Firestore data is fetching.
 */
@Composable
fun ShimmerHomeScreenSkeleton(
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 0.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .testTag("home_shimmer_skeleton")
    ) {
        // Hero Section Skeleton
        item {
            ShimmerHeroBannerSkeleton()
        }

        // Continue Watching Rail Skeleton
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                ShimmerBox(modifier = Modifier.width(170.dp).height(18.dp), shape = RoundedCornerShape(4.dp))
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBox(modifier = Modifier.width(120.dp).height(11.dp), shape = RoundedCornerShape(3.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) {
                    ShimmerContinueWatchingCardSkeleton()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Trending Now Rail Skeleton
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                ShimmerBox(modifier = Modifier.width(150.dp).height(18.dp), shape = RoundedCornerShape(4.dp))
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBox(modifier = Modifier.width(180.dp).height(11.dp), shape = RoundedCornerShape(3.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) {
                    ShimmerRailCardSkeleton()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Suggested For You Rail Skeleton
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                ShimmerBox(modifier = Modifier.width(160.dp).height(18.dp), shape = RoundedCornerShape(4.dp))
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBox(modifier = Modifier.width(200.dp).height(11.dp), shape = RoundedCornerShape(3.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(4) {
                    ShimmerRailCardSkeleton(cardWidth = 200.dp, cardHeight = 110.dp)
                }
            }
        }
    }
}
