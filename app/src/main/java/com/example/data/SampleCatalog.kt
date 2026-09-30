package com.example.data

object SampleCatalog {
    // Reliable bundled local MP4 test stream guaranteeing offline & emulator playback without MediaPlayer errors
    const val LOCAL_STREAM_URI = "android.resource://com.example/raw/sample_stream"
    private const val STREAM_ANIMATION = LOCAL_STREAM_URI
    private const val STREAM_SCIFI = LOCAL_STREAM_URI
    private const val STREAM_FANTASY = LOCAL_STREAM_URI
    private const val STREAM_NATURE = LOCAL_STREAM_URI

    val featuredShow = Show(
        id = "show_solo_leveling",
        title = "Solo Leveling: Arise",
        japaneseOrKoreanTitle = "나 혼자만 레벨업",
        category = "Anime",
        genres = listOf("Action", "Dark Fantasy", "Supernatural", "Shonen"),
        bannerUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&q=80",
        posterUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&q=80",
        matchScore = 98,
        ratingScore = 9.9,
        year = "2025",
        ageRating = "18+",
        qualityTag = "1080P MASTER",
        audioBadge = "DOLBY ATMOS",
        description = "In a world where hunters awakened with supernatural abilities battle deadly monsters, Sung Jinwoo, notoriously known as the weakest hunter of all mankind, is selected by the mysterious System as its sole player.",
        isTop10 = true,
        top10Rank = 1,
        cast = listOf("Taito Ban", "Genta Nakamura", "Reina Ueda", "Daisuke Hirakawa"),
        seasons = listOf(
            Season(
                seasonNumber = 1,
                title = "Season 1: Awakening",
                year = "2024",
                episodeCount = 12,
                episodes = listOf(
                    Episode(
                        id = "sl_s1_e1",
                        episodeNumber = 1,
                        title = "I'm Used to It",
                        durationText = "24m",
                        durationSeconds = 1440,
                        summary = "Jinwoo joins a low-rank raid squad into a D-Rank dungeon, but uncovers a hidden temple with rules of terror.",
                        videoUrl = STREAM_FANTASY,
                        thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&q=80",
                        downloadSizeMb = 480
                    ),
                    Episode(
                        id = "sl_s1_e2",
                        episodeNumber = 2,
                        title = "If I Had One More Chance",
                        durationText = "23m",
                        durationSeconds = 1380,
                        summary = "Trapped in the double dungeon, the survivors must solve the ancient statues' riddle before time runs out.",
                        videoUrl = STREAM_ANIMATION,
                        thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&q=80",
                        downloadSizeMb = 460
                    ),
                    Episode(
                        id = "sl_s1_e3",
                        episodeNumber = 3,
                        title = "It's Like a Game",
                        durationText = "25m",
                        durationSeconds = 1500,
                        summary = "Jinwoo awakens in a hospital bed to find a floating quest log that only he can see.",
                        videoUrl = STREAM_SCIFI,
                        thumbnailUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=500&q=80",
                        downloadSizeMb = 510
                    ),
                    Episode(
                        id = "sl_s1_e4",
                        episodeNumber = 4,
                        title = "I've Gotta Get Stronger",
                        durationText = "24m",
                        durationSeconds = 1440,
                        summary = "Venturing alone into an underground subway station, Jinwoo confronts ferocious demon beasts.",
                        videoUrl = STREAM_NATURE,
                        thumbnailUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=500&q=80",
                        downloadSizeMb = 475
                    ),
                    Episode(
                        id = "sl_s1_e5",
                        episodeNumber = 5,
                        title = "A Pretty Good Deal",
                        durationText = "24m",
                        durationSeconds = 1440,
                        summary = "Jinwoo joins Hwang Dongsuk's raid party as a porter, unaware of the sinister betrayal planned.",
                        videoUrl = STREAM_FANTASY,
                        thumbnailUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=500&q=80",
                        downloadSizeMb = 490
                    )
                )
            ),
            Season(
                seasonNumber = 2,
                title = "Season 2: Arise from the Shadow",
                year = "2025",
                episodeCount = 13,
                episodes = listOf(
                    Episode(
                        id = "sl_s2_e1",
                        episodeNumber = 1,
                        title = "You Aren't an E-Rank, Are You?",
                        durationText = "25m",
                        durationSeconds = 1500,
                        summary = "Jinwoo undergoes the hunter re-evaluation test, triggering seismic shockwaves across the entire Association.",
                        videoUrl = STREAM_SCIFI,
                        thumbnailUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=500&q=80",
                        downloadSizeMb = 520
                    ),
                    Episode(
                        id = "sl_s2_e2",
                        episodeNumber = 2,
                        title = "The Red Gate",
                        durationText = "24m",
                        durationSeconds = 1440,
                        summary = "A training gate mutates into an inescapable Red Gate freezing the squad in an icy tundra wasteland.",
                        videoUrl = STREAM_ANIMATION,
                        thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&q=80",
                        downloadSizeMb = 495
                    ),
                    Episode(
                        id = "sl_s2_e3",
                        episodeNumber = 3,
                        title = "Demon Castle Infiltration",
                        durationText = "26m",
                        durationSeconds = 1560,
                        summary = "Jinwoo unlocks the key to the multi-level Demon Castle to craft the Purified Water of Life.",
                        videoUrl = STREAM_FANTASY,
                        thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&q=80",
                        downloadSizeMb = 540
                    ),
                    Episode(
                        id = "sl_s2_e4",
                        episodeNumber = 4,
                        title = "Monarch of Shadows",
                        durationText = "27m",
                        durationSeconds = 1620,
                        summary = "Facing the Blood-Red Commander Igris, Jinwoo must extract the soul to summon his greatest knight.",
                        videoUrl = STREAM_NATURE,
                        thumbnailUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=500&q=80",
                        downloadSizeMb = 580
                    )
                )
            ),
            Season(
                seasonNumber = 3,
                title = "Season 3: Jeju Island Monarchs",
                year = "2026",
                episodeCount = 10,
                episodes = listOf(
                    Episode(
                        id = "sl_s3_e1",
                        episodeNumber = 1,
                        title = "The Ant King Awakes",
                        durationText = "28m",
                        durationSeconds = 1680,
                        summary = "The S-Rank raid on Jeju Island descends into panic when an evolved winged predator emerges.",
                        videoUrl = STREAM_SCIFI,
                        thumbnailUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=500&q=80",
                        downloadSizeMb = 610
                    )
                )
            )
        )
    )

    val kdramaQueenOfTears = Show(
        id = "show_queen_of_tears",
        title = "Queen of Tears",
        japaneseOrKoreanTitle = "눈물의 여왕",
        category = "Kdrama",
        genres = listOf("Romance", "Drama", "Melodrama", "Comedy"),
        bannerUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&q=80",
        posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&q=80",
        matchScore = 99,
        ratingScore = 9.8,
        year = "2024",
        ageRating = "16+",
        qualityTag = "4K UHD HDR",
        audioBadge = "DOLBY ATMOS",
        description = "The queen of department stores and the prince of supermarkets weather a marital crisis until love miraculously begins to bloom again against all odds.",
        isTop10 = true,
        top10Rank = 2,
        cast = listOf("Kim Soo-hyun", "Kim Ji-won", "Park Sung-hoon", "Kwak Dong-yeon"),
        seasons = listOf(
            Season(
                seasonNumber = 1,
                title = "Season 1",
                year = "2024",
                episodeCount = 16,
                episodes = listOf(
                    Episode(
                        id = "qot_s1_e1",
                        episodeNumber = 1,
                        title = "The Miraculous Proposal",
                        durationText = "72m",
                        durationSeconds = 4320,
                        summary = "Hyun-woo feels alienated in Hae-in's conglomerate family and contemplates filing for divorce.",
                        videoUrl = STREAM_ANIMATION,
                        thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&q=80",
                        downloadSizeMb = 820
                    ),
                    Episode(
                        id = "qot_s1_e2",
                        episodeNumber = 2,
                        title = "Three Months to Live",
                        durationText = "75m",
                        durationSeconds = 4500,
                        summary = "A shocking medical diagnosis forces Hyun-woo to re-examine his true feelings for Hae-in.",
                        videoUrl = STREAM_SCIFI,
                        thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&q=80",
                        downloadSizeMb = 850
                    )
                )
            )
        )
    )

    val animeCyberpunk = Show(
        id = "show_cyberpunk",
        title = "Cyberpunk: Neon Horizon",
        japaneseOrKoreanTitle = "サイバーパンク ネオン",
        category = "Anime",
        genres = listOf("Sci-Fi", "Cyberpunk", "Action", "Dystopian"),
        bannerUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=1200&q=80",
        posterUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=600&q=80",
        matchScore = 97,
        ratingScore = 9.7,
        year = "2024",
        ageRating = "18+",
        qualityTag = "1080P MASTER",
        audioBadge = "5.1 SURROUND",
        description = "A street kid trying to survive in Night City, a technology and body modification-obsessed city of the future, chooses to become an edgerunner.",
        isTop10 = true,
        top10Rank = 3,
        cast = listOf("KENN", "Aoi Yuuki", "Hiroki Touchi", "Michiko Kaiden"),
        seasons = listOf(
            Season(
                seasonNumber = 1,
                title = "Season 1",
                year = "2024",
                episodeCount = 10,
                episodes = listOf(
                    Episode(
                        id = "cp_s1_e1",
                        episodeNumber = 1,
                        title = "Sandevistan Overdrive",
                        durationText = "24m",
                        durationSeconds = 1440,
                        summary = "David installs military-grade chrome, pushing his body to the absolute limits of cyberpsychosis.",
                        videoUrl = STREAM_SCIFI,
                        thumbnailUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=500&q=80",
                        downloadSizeMb = 450
                    )
                )
            )
        )
    )

    val kdramaSquidGame = Show(
        id = "show_squid_game_2",
        title = "Squid Game: Season 2",
        japaneseOrKoreanTitle = "오징어 게임 2",
        category = "Kdrama",
        genres = listOf("Thriller", "Mystery", "Survival", "Suspense"),
        bannerUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=1200&q=80",
        posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&q=80",
        matchScore = 96,
        ratingScore = 9.6,
        year = "2025",
        ageRating = "18+",
        qualityTag = "4K UHD HDR",
        audioBadge = "DOLBY ATMOS",
        description = "Gi-hun abandons his plans to go to the US and starts a chase with a motive after the shadowy masters of the deadly game.",
        isTop10 = true,
        top10Rank = 4,
        cast = listOf("Lee Jung-jae", "Lee Byung-hun", "Wi Ha-joon", "Im Si-wan"),
        seasons = listOf(
            Season(
                seasonNumber = 1,
                title = "Season 1",
                year = "2021",
                episodeCount = 9,
                episodes = listOf(
                    Episode(
                        id = "sg_s1_e1",
                        episodeNumber = 1,
                        title = "Red Light, Green Light",
                        durationText = "59m",
                        durationSeconds = 3540,
                        summary = "Hundreds of cash-strapped players accept a strange invitation to compete in children's games.",
                        videoUrl = STREAM_FANTASY,
                        thumbnailUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=500&q=80",
                        downloadSizeMb = 750
                    )
                )
            ),
            Season(
                seasonNumber = 2,
                title = "Season 2",
                year = "2025",
                episodeCount = 6,
                episodes = listOf(
                    Episode(
                        id = "sg_s2_e1",
                        episodeNumber = 1,
                        title = "Bread and Lottery",
                        durationText = "64m",
                        durationSeconds = 3840,
                        summary = "Gi-hun re-enters the lethal arena determined to destroy the game from the inside.",
                        videoUrl = STREAM_NATURE,
                        thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&q=80",
                        downloadSizeMb = 790
                    )
                )
            )
        )
    )

    val animeJujutsu = Show(
        id = "show_jujutsu_kaisen",
        title = "Jujutsu Kaisen: Shibuya Arc",
        japaneseOrKoreanTitle = "呪術廻戦",
        category = "Anime",
        genres = listOf("Supernatural", "Action", "Dark Fantasy"),
        bannerUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=1200&q=80",
        posterUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&q=80",
        matchScore = 99,
        ratingScore = 9.9,
        year = "2024",
        ageRating = "18+",
        qualityTag = "1080P MASTER",
        audioBadge = "DOLBY ATMOS",
        description = "On October 31st, a curtain drops over Shibuya, trapping countless civilians. Satoru Gojo descends into the subway station.",
        isTop10 = true,
        top10Rank = 5,
        cast = listOf("Junya Enoki", "Yuma Uchida", "Asami Seto", "Yuichi Nakamura"),
        seasons = listOf(
            Season(
                seasonNumber = 2,
                title = "Season 2",
                year = "2024",
                episodeCount = 23,
                episodes = listOf(
                    Episode(
                        id = "jjk_s2_e1",
                        episodeNumber = 1,
                        title = "Hidden Inventory",
                        durationText = "24m",
                        durationSeconds = 1440,
                        summary = "Spring 2006. Gojo and Geto are assigned an escort mission for the Star Plasma Vessel.",
                        videoUrl = STREAM_SCIFI,
                        thumbnailUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=500&q=80",
                        downloadSizeMb = 480
                    ),
                    Episode(
                        id = "jjk_s2_e9",
                        episodeNumber = 9,
                        title = "Shibuya Incident: Gate Open",
                        durationText = "24m",
                        durationSeconds = 1440,
                        summary = "The cursed spirits unleash their ultimate trump card to seal Gojo Satoru.",
                        videoUrl = STREAM_ANIMATION,
                        thumbnailUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=500&q=80",
                        downloadSizeMb = 510
                    )
                )
            )
        )
    )

    val movieInterstellar = Show(
        id = "show_interstellar",
        title = "Interstellar: Beyond Abyss",
        japaneseOrKoreanTitle = "인터스텔라",
        category = "Movies",
        genres = listOf("Sci-Fi", "Adventure", "Space", "Drama"),
        bannerUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1200&q=80",
        posterUrl = "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=600&q=80",
        matchScore = 98,
        ratingScore = 9.8,
        year = "2024",
        ageRating = "13+",
        qualityTag = "4K IMAX ENHANCED",
        audioBadge = "DOLBY ATMOS",
        description = "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans.",
        isTop10 = false,
        cast = listOf("Matthew McConaughey", "Anne Hathaway", "Jessica Chastain", "Michael Caine"),
        seasons = listOf(
            Season(
                seasonNumber = 1,
                title = "Feature Film",
                year = "2024",
                episodeCount = 1,
                episodes = listOf(
                    Episode(
                        id = "int_film",
                        episodeNumber = 1,
                        title = "Full Movie (IMAX Remastered)",
                        durationText = "169m",
                        durationSeconds = 10140,
                        summary = "A journey across the wormhole to save human existence.",
                        videoUrl = STREAM_SCIFI,
                        thumbnailUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=500&q=80",
                        downloadSizeMb = 2800
                    )
                )
            )
        )
    )

    val allCatalog = listOf(
        featuredShow,
        kdramaQueenOfTears,
        animeCyberpunk,
        kdramaSquidGame,
        animeJujutsu,
        movieInterstellar
    )

    val initialDownloads = listOf(
        DownloadItem(
            id = "dl_1",
            showId = featuredShow.id,
            showTitle = featuredShow.title,
            episodeNumber = 1,
            episodeTitle = "I'm Used to It",
            thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&q=80",
            sizeMb = 480,
            progress = 1.0f,
            status = DownloadStatus.COMPLETED,
            quality = "1080p Master"
        ),
        DownloadItem(
            id = "dl_2",
            showId = featuredShow.id,
            showTitle = featuredShow.title,
            episodeNumber = 2,
            episodeTitle = "If I Had One More Chance",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&q=80",
            sizeMb = 460,
            progress = 1.0f,
            status = DownloadStatus.COMPLETED,
            quality = "1080p Master"
        ),
        DownloadItem(
            id = "dl_3",
            showId = animeCyberpunk.id,
            showTitle = animeCyberpunk.title,
            episodeNumber = 1,
            episodeTitle = "Sandevistan Overdrive",
            thumbnailUrl = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=500&q=80",
            sizeMb = 450,
            progress = 0.68f,
            status = DownloadStatus.DOWNLOADING,
            quality = "1080p Master"
        )
    )

    val initialWatchHistory = listOf(
        WatchHistoryItem(
            showId = featuredShow.id,
            showTitle = featuredShow.title,
            episodeTitle = "EP 02: If I Had One More Chance",
            episodeNumber = 2,
            progressFraction = 0.75f,
            lastWatchedText = "Watched today",
            posterUrl = featuredShow.posterUrl
        ),
        WatchHistoryItem(
            showId = kdramaQueenOfTears.id,
            showTitle = kdramaQueenOfTears.title,
            episodeTitle = "EP 01: The Miraculous Proposal",
            episodeNumber = 1,
            progressFraction = 0.42f,
            lastWatchedText = "Watched yesterday",
            posterUrl = kdramaQueenOfTears.posterUrl
        ),
        WatchHistoryItem(
            showId = animeJujutsu.id,
            showTitle = animeJujutsu.title,
            episodeTitle = "EP 09: Shibuya Incident",
            episodeNumber = 9,
            progressFraction = 0.90f,
            lastWatchedText = "Watched 3 days ago",
            posterUrl = animeJujutsu.posterUrl
        )
    )
}
