package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CreatorStudioStats
import com.example.data.Season
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanSubtle
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TagGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonSelectorBottomSheet(
    seasons: List<Season>,
    selectedSeasonNumber: Int,
    onSeasonSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECT SEASON",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                items(seasons) { season ->
                    val isSelected = season.seasonNumber == selectedSeasonNumber
                    val borderColor = if (isSelected) NeonCyan else DarkBorder
                    val bgColor = if (isSelected) NeonCyanSubtle else DarkSurface

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("season_item_${season.seasonNumber}")
                            .clip(RoundedCornerShape(14.dp))
                            .background(bgColor)
                            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable { onSeasonSelected(season.seasonNumber) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = season.title,
                                color = if (isSelected) NeonCyan else TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${season.episodeCount} Episodes • ${season.year}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color(0xFF001B20),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSubtitleBottomSheet(
    selectedAudio: String,
    selectedSubtitle: String,
    onAudioSelected: (String) -> Unit,
    onSubtitleSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val audioOptions = listOf(
        "Japanese [Original] (Dolby Atmos)",
        "English [Dub] (Dolby Audio 5.1)",
        "Korean [Original] (Stereo)",
        "Spanish (Dub)"
    )

    val subtitleOptions = listOf(
        "English [CC]",
        "Spanish",
        "Korean",
        "Japanese",
        "Off"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "AUDIO & SUBTITLES",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Audio Track",
                color = NeonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            audioOptions.forEach { track ->
                val isSelected = track == selectedAudio
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) NeonCyanSubtle else Color.Transparent)
                        .clickable { onAudioSelected(track) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track,
                        color = if (isSelected) NeonCyan else TextPrimary,
                        fontSize = 14.sp
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Subtitles",
                color = NeonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            subtitleOptions.forEach { sub ->
                val isSelected = sub == selectedSubtitle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) NeonCyanSubtle else Color.Transparent)
                        .clickable { onSubtitleSelected(sub) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sub,
                        color = if (isSelected) NeonCyan else TextPrimary,
                        fontSize = 14.sp
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorStudioBottomSheet(
    onDismiss: () -> Unit
) {
    val stats = remember { CreatorStudioStats() }
    var streamKeyVisible by remember { mutableStateOf(false) }
    var isSimulatingIngest by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0D0D15),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(NeonPurple.copy(alpha = 0.5f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(TagGreen, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CREATOR STUDIO & INGEST GATE",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Realtime metrics grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(text = "Edge Latency", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${stats.edgeLatencyMs} ms", color = TagGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Global CDN", color = TextMuted, fontSize = 10.sp)
                    }
                }

                // Card 2
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(text = "Ingest Health", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${stats.ingestHealthPercent}%", color = NeonCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Zero dropped frames", color = TextMuted, fontSize = 10.sp)
                    }
                }

                // Card 3
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(text = "Active Nodes", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${stats.activeCdnNodes}", color = NeonPurple, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Tokyo / Seoul / US", color = TextMuted, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stream Key section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "RTMP Ingest Key", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = if (streamKeyVisible) "HIDE" else "REVEAL",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { streamKeyVisible = !streamKeyVisible }
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (streamKeyVisible) stats.streamKey else "live_sx_••••••••••••••••",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Endpoint: rtmp://ingest.streamx.cloud/live/master",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bitrate & Master Ingest
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Master Broadcast Ingest", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = stats.bitrate, color = TextSecondary, fontSize = 12.sp)
                }
                Button(
                    onClick = { isSimulatingIngest = !isSimulatingIngest },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSimulatingIngest) TagGreen else NeonPurple
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (isSimulatingIngest) "BROADCASTING" else "START TEST INGEST",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CastDeviceDialog(
    onDismiss: () -> Unit
) {
    val devices = listOf(
        "Living Room Apple TV 4K",
        "Samsung OLED 65\" (AirPlay 2)",
        "Google TV Streamer (4K HDR)",
        "Bedroom Roku Ultra"
    )
    var selectedDevice by remember { mutableStateOf(devices.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Tv, contentDescription = null, tint = NeonCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Cast to Device", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Select a target screen on your local Wi-Fi network:", color = TextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                devices.forEach { device ->
                    val isChosen = device == selectedDevice
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isChosen) NeonCyanSubtle else DarkSurface)
                            .clickable { selectedDevice = device }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = device, color = if (isChosen) NeonCyan else TextPrimary, fontSize = 13.sp)
                        if (isChosen) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF001B20)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Connect", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = TextSecondary)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterSheet(
    onDismiss: () -> Unit
) {
    val notifications = listOf(
        Pair("Solo Leveling: Season 2 Released!", "Episode 1 to 4 are now available in 1080p Master 60fps with Dolby Atmos audio."),
        Pair("Queen of Tears Finale Special", "Watch the exclusive cast commentary and deleted scenes in 4K HDR."),
        Pair("Offline Vault Sync Complete", "Episode 2 of Solo Leveling has been downloaded for offline viewing.")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = NeonCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "NOTIFICATIONS", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            notifications.forEach { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(text = item.first, color = NeonCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.second, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
