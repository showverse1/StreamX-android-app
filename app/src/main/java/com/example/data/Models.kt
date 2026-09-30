package com.example.data

data class Episode(
    val id: String,
    val episodeNumber: Int,
    val title: String,
    val durationText: String,
    val durationSeconds: Int,
    val summary: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val downloadSizeMb: Int
)

data class Season(
    val seasonNumber: Int,
    val title: String,
    val year: String,
    val episodeCount: Int,
    val episodes: List<Episode>
)

data class Show(
    val id: String,
    val title: String,
    val japaneseOrKoreanTitle: String = "",
    val category: String, // "Anime", "Kdrama", "Movies"
    val genres: List<String>,
    val bannerUrl: String,
    val posterUrl: String,
    val matchScore: Int, // e.g. 98 -> 98% Match
    val ratingScore: Double, // e.g. 9.9
    val year: String,
    val ageRating: String, // "18+", "16+"
    val qualityTag: String, // "1080P MASTER", "4K UHD"
    val audioBadge: String, // "DOLBY ATMOS", "5.1 DUB"
    val description: String,
    val seasons: List<Season>,
    val cast: List<String>,
    val isTop10: Boolean = false,
    val top10Rank: Int = 0
)

enum class DownloadStatus {
    DOWNLOADING,
    COMPLETED,
    PAUSED
}

data class DownloadItem(
    val id: String,
    val showId: String,
    val showTitle: String,
    val episodeNumber: Int,
    val episodeTitle: String,
    val thumbnailUrl: String,
    val sizeMb: Int,
    val progress: Float, // 0.0 to 1.0
    val status: DownloadStatus,
    val quality: String = "1080p Master"
)

data class WatchHistoryItem(
    val showId: String,
    val showTitle: String,
    val episodeTitle: String,
    val episodeNumber: Int,
    val progressFraction: Float, // e.g. 0.65
    val lastWatchedText: String,
    val posterUrl: String
)

data class UserProfile(
    val name: String = "Alex Mercer",
    val email: String = "alex.mercer@streamx.io",
    val avatarUrl: String? = null,
    val membershipTier: String = "STREAMX VIP PLATINUM",
    val isVip: Boolean = true,
    val expiryDate: String = "October 2026",
    val wifiOnlyDownloads: Boolean = true,
    val smartDownloads: Boolean = true,
    val streamQuality: String = "Ultra HD 4K (Auto)",
    val cacheSizeMb: Double = 1420.0
)

data class CreatorStudioStats(
    val activeCdnNodes: Int = 128,
    val ingestHealthPercent: Double = 99.98,
    val edgeLatencyMs: Int = 16,
    val activeViewers: String = "2.4M",
    val bitrate: String = "18.5 Mbps (HEVC/H.265)",
    val streamKey: String = "live_sx_89a0b3f71c498d2e"
)
