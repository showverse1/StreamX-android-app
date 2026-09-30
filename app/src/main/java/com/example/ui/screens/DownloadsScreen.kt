package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.DownloadItem
import com.example.data.DownloadStatus
import com.example.data.Show
import com.example.ui.components.QualityPill
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanSubtle
import com.example.ui.theme.TagGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DownloadsScreen(
    downloads: List<DownloadItem>,
    catalog: List<Show>,
    smartDownloads: Boolean,
    onToggleSmartDownloads: () -> Unit,
    onTogglePause: (String) -> Unit,
    onDeleteDownload: (String) -> Unit,
    onPlayDownload: (Show, Int) -> Unit,
    contentPadding: PaddingValues
) {
    val totalSizeMb = downloads.sumOf { it.sizeMb }
    val totalSizeGb = totalSizeMb / 1024.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .statusBarsPadding()
            .padding(bottom = contentPadding.calculateBottomPadding())
    ) {
        // Top Header
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(NeonCyanSubtle, RoundedCornerShape(8.dp))
                            .border(1.dp, NeonCyan, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderZip,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "OFFLINE VAULT",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "${downloads.size} Items",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Storage Meter Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Device Storage",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = String.format("%.2f GB of 64 GB used", 4.2 + totalSizeGb),
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { ((4.2 + totalSizeGb) / 64.0).toFloat().coerceIn(0f, 1f) },
                        color = NeonCyan,
                        trackColor = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Smart Downloads toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Smart Downloads",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Auto-delete watched & save next episode",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
                Switch(
                    checked = smartDownloads,
                    onCheckedChange = { onToggleSmartDownloads() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeonCyan,
                        checkedTrackColor = NeonCyanSubtle,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = DarkSurface
                    )
                )
            }
        }

        // List of currently downloaded or downloading episodes
        if (downloads.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.DownloadDone,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Offline Vault is Empty",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Download episodes from the player to watch anywhere without internet.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(downloads, key = { it.id }) { item ->
                    val show = catalog.find { it.id == item.showId } ?: catalog.first()

                    DownloadItemRow(
                        item = item,
                        onPlay = { onPlayDownload(show, item.episodeNumber) },
                        onTogglePause = { onTogglePause(item.id) },
                        onDelete = { onDeleteDownload(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DownloadItemRow(
    item: DownloadItem,
    onPlay: () -> Unit,
    onTogglePause: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("download_row_${item.id}")
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 68.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onPlay)
            ) {
                AsyncImage(
                    model = item.thumbnailUrl,
                    contentDescription = item.episodeTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Status overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp)
                        .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                        .border(1.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Metadata: Title, episode, size, progress
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.showTitle,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "EP ${item.episodeNumber}: ${item.episodeTitle}",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    QualityPill(text = item.quality, isNeon = true)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${item.sizeMb} MB",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                // If downloading, show linear progress
                if (item.status == DownloadStatus.DOWNLOADING) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { item.progress },
                        color = NeonCyan,
                        trackColor = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action: Circular indicator or Completed Icon + Delete button
            Row(verticalAlignment = Alignment.CenterVertically) {
                when (item.status) {
                    DownloadStatus.COMPLETED -> {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clickable(onClick = onPlay)
                                .background(NeonCyanSubtle, CircleShape)
                                .border(1.dp, NeonCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play offline",
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    DownloadStatus.DOWNLOADING -> {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clickable(onClick = onTogglePause),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { item.progress },
                                color = NeonCyan,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(32.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = "Pause",
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    DownloadStatus.PAUSED -> {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clickable(onClick = onTogglePause)
                                .background(DarkSurface, CircleShape)
                                .border(1.dp, DarkBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Resume",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("delete_download_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
