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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.UserProfile
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.theme.DarkBg
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
import com.example.ui.theme.VipGoldEnd
import com.example.ui.theme.VipGoldStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    onToggleWifiOnly: () -> Unit,
    onClearCache: () -> Unit,
    onOpenCreatorStudio: () -> Unit,
    onOpenAuth: () -> Unit = {},
    onUpdateProfile: (String, String?) -> Unit = { _, _ -> },
    contentPadding: PaddingValues
) {
    var showHelpDialog by remember { mutableStateOf(false) }
    var cacheClearedMessage by remember { mutableStateOf(false) }
    var showEditProfileSheet by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .statusBarsPadding(),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 28.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Avatar and Name header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Avatar with Neon Cyan glow border and Camera/Edit overlay
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(listOf(NeonCyan, NeonPurple, VipGoldStart, NeonCyan))
                        )
                        .padding(2.5.dp)
                        .clickable { showEditProfileSheet = true }
                        .testTag("profile_avatar_image")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(DarkSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!profile.avatarUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = profile.avatarUrl,
                                contentDescription = "Profile Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            val initials = profile.name.split(" ")
                                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                                .take(2)
                                .joinToString("")
                            Text(
                                text = if (initials.isNotEmpty()) initials else "AM",
                                color = NeonCyan,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Small edit camera badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(NeonCyan)
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Change photo",
                                tint = Color(0xFF001B20),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = profile.name,
                            color = TextPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(TagGreen, CircleShape)
                        )
                        // Edit Profile Pill Button
                        Box(
                            modifier = Modifier
                                .testTag("edit_profile_button")
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeonCyan.copy(alpha = 0.15f))
                                .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .clickable { showEditProfileSheet = true }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "EDIT",
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = profile.email,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onOpenAuth() }
                    )
                }
            }
        }

        // "VIP MEMBERSHIP ACTIVE" GOLDEN / CYAN GRADIENT CARD
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vip_membership_card")
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF1E1700),
                                Color(0xFF261905),
                                Color(0xFF0D1C24)
                            )
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(listOf(VipGoldStart, NeonCyan)),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(VipGoldStart, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFF2B1D00),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = profile.membershipTier,
                                color = VipGoldStart,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                color = NeonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Ultra HD 4K • 4 Simultaneous Screens • Dolby Atmos Audio & Uncapped Master Downloads",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Auto-renews: ${profile.expiryDate}",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Section: Account & Playback Settings
        item {
            Text(
                text = "APP SETTINGS & PREFERENCES",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Account & Cloud Sync Tile
        item {
            ProfileSettingTile(
                icon = Icons.Default.CloudDone,
                title = "StreamX Account & Cloud Login",
                subtitle = "Manage VIP subscription, watch history & library backup",
                onClick = onOpenAuth,
                trailing = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(TagGreen.copy(alpha = 0.18f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "ONLINE",
                            color = TagGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            )
        }

        // Wi-Fi Only Downloads Toggle Tile
        item {
            ProfileSettingTile(
                icon = Icons.Default.Wifi,
                title = "Wi-Fi Only Downloads",
                subtitle = "Prevent cellular data usage when saving offline",
                trailing = {
                    Switch(
                        checked = profile.wifiOnlyDownloads,
                        onCheckedChange = { onToggleWifiOnly() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonCyan,
                            checkedTrackColor = NeonCyanSubtle,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = DarkSurface
                        ),
                        modifier = Modifier.testTag("wifi_only_toggle")
                    )
                }
            )
        }

        // Video Streaming Quality Tile
        item {
            ProfileSettingTile(
                icon = Icons.Default.Settings,
                title = "Default Video Quality",
                subtitle = profile.streamQuality,
                trailing = {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            )
        }

        // Watch History Tile
        item {
            ProfileSettingTile(
                icon = Icons.Default.History,
                title = "Watch History",
                subtitle = "Manage recently viewed episodes and movies",
                trailing = {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
            )
        }

        // Clear Cache Tile
        item {
            ProfileSettingTile(
                icon = Icons.Default.CleaningServices,
                title = "Clear Cache",
                subtitle = if (cacheClearedMessage) "Cache cleared to 12.4 MB!" else "Temporary video chunks: ${String.format("%.1f MB", profile.cacheSizeMb)}",
                trailing = {
                    Text(
                        text = "CLEAR",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .testTag("clear_cache_button")
                            .clickable {
                                onClearCache()
                                cacheClearedMessage = true
                            }
                    )
                }
            )
        }

        // Help & Support
        item {
            ProfileSettingTile(
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                title = "Help & Live Support",
                subtitle = "Stream speed test, FAQs, and concierge",
                trailing = {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                onClick = { showHelpDialog = true }
            )
        }

        // Privacy & Terms
        item {
            ProfileSettingTile(
                icon = Icons.Default.Security,
                title = "DRM & License Security",
                subtitle = "Widevine L1 Certified • 1080p Master Supported",
                trailing = {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(TagGreen, CircleShape)
                    )
                }
            )
        }

        // SECRET ADMIN GATE: At the very bottom, include a subdued/faded text button that says "Creator Studio"
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "StreamX Native Client v3.4.1 (Build 8901)",
                    color = TextMuted,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                // FADED / SUBDUED SECRET CREATOR STUDIO BUTTON
                TextButton(
                    onClick = onOpenCreatorStudio,
                    modifier = Modifier.testTag("creator_studio_secret_gate")
                ) {
                    Text(
                        text = "Creator Studio",
                        color = TextMuted.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }

    if (showEditProfileSheet) {
        EditProfileBottomSheet(
            currentName = profile.name,
            currentAvatarUrl = profile.avatarUrl,
            onDismiss = { showEditProfileSheet = false },
            onSave = { newName, newAvatarUrl ->
                onUpdateProfile(newName, newAvatarUrl)
                showEditProfileSheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBottomSheet(
    currentName: String,
    currentAvatarUrl: String?,
    onDismiss: () -> Unit,
    onSave: (newName: String, newAvatarUrl: String?) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var selectedAvatar by remember {
        mutableStateOf(currentAvatarUrl ?: "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=300")
    }
    var customUrl by remember { mutableStateOf("") }

    val presetAvatars = listOf(
        "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=300" to "Cyberpunk Neon",
        "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=300" to "Anime Blade",
        "https://images.unsplash.com/photo-1563089145-599997674d42?w=300" to "Sci-Fi Pilot",
        "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=300" to "Shadow Agent"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        scrimColor = Color.Black.copy(alpha = 0.75f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .testTag("edit_profile_modal_sheet")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EDIT PROFILE",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Syncs with your StreamX Account securely",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Avatar Preview with live ring
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(Brush.sweepGradient(listOf(NeonCyan, NeonPurple, VipGoldStart, NeonCyan)))
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(DarkSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = selectedAvatar,
                            contentDescription = "Avatar Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Choose Avatar Presets
            Text(
                text = "CHOOSE AVATAR PRESET",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                presetAvatars.forEach { (url, label) ->
                    val isSelected = selectedAvatar == url
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, if (isSelected) NeonCyan else DarkBorder, CircleShape)
                            .clickable {
                                selectedAvatar = url
                                customUrl = ""
                            }
                            .padding(2.dp)
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = label,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Display Name", color = TextSecondary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = DarkBorder,
                    focusedContainerColor = DarkSurfaceElevated,
                    unfocusedContainerColor = DarkSurfaceElevated
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("edit_profile_name_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Custom Avatar URL input
            OutlinedTextField(
                value = customUrl,
                onValueChange = {
                    customUrl = it
                    if (it.isNotBlank()) selectedAvatar = it.trim()
                },
                label = { Text("Or Custom Photo URL", color = TextSecondary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = DarkBorder,
                    focusedContainerColor = DarkSurfaceElevated,
                    unfocusedContainerColor = DarkSurfaceElevated
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("edit_profile_photo_url_input")
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Save Button
            NeonPrimaryButton(
                text = "SAVE CHANGES & SYNC",
                icon = Icons.Default.Check,
                onClick = {
                    val finalName = if (name.isNotBlank()) name.trim() else currentName
                    val finalAvatar = if (customUrl.isNotBlank()) customUrl.trim() else selectedAvatar
                    onSave(finalName, finalAvatar)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_profile_button")
            )
        }
    }
}

@Composable
fun ProfileSettingTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
            .then(clickModifier)
            .padding(horizontal = 14.dp, vertical = 13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            trailing()
        }
    }
}
