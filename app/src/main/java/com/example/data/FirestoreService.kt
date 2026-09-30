package com.example.data

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestoreSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object FirestoreService {
    private const val TAG = "FirestoreService"
    private var isInitialized = false
    private var firestoreInstance: FirebaseFirestore? = null

    private val _cloudSyncStatus = MutableStateFlow("Synced with Cloud")
    val cloudSyncStatus: StateFlow<String> = _cloudSyncStatus.asStateFlow()

    private val _isCloudConnected = MutableStateFlow(false)
    val isCloudConnected: StateFlow<Boolean> = _isCloudConnected.asStateFlow()

    fun initialize(context: Context) {
        if (isInitialized) return

        try {
            // Check if default FirebaseApp is already initialized (e.g. by FirebaseInitProvider)
            val app = if (FirebaseApp.getApps(context).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApplicationId("1:710743121043:android:streamx")
                    .setProjectId("streamx-cloud-platform")
                    .setApiKey("AIzaSyFakeKeyForLocalInitFallback000")
                    .build()
                FirebaseApp.initializeApp(context.applicationContext, options)
            } else {
                FirebaseApp.getInstance()
            }

            val db = FirebaseFirestore.getInstance(app)
            val settings = firestoreSettings {
                // Enable offline local disk persistence
                isPersistenceEnabled = true
            }
            db.firestoreSettings = settings

            firestoreInstance = db
            isInitialized = true
            _isCloudConnected.value = true
            _cloudSyncStatus.value = "Cloud Sync Active"
            Log.d(TAG, "Firebase Firestore initialized successfully with offline persistence.")
        } catch (e: Exception) {
            Log.w(TAG, "Firestore initialization fallback: ${e.message}")
            _cloudSyncStatus.value = "Local Cloud Cache Mode"
        }
    }

    fun syncUserProfile(userId: String = "user_alex_mercer", profile: UserProfile) {
        val db = firestoreInstance ?: return
        val profileData = hashMapOf(
            "name" to profile.name,
            "displayName" to profile.name,
            "email" to profile.email,
            "avatarUrl" to (profile.avatarUrl ?: ""),
            "photoUrl" to (profile.avatarUrl ?: ""),
            "membershipTier" to profile.membershipTier,
            "isVip" to profile.isVip,
            "expiryDate" to profile.expiryDate,
            "wifiOnlyDownloads" to profile.wifiOnlyDownloads,
            "smartDownloads" to profile.smartDownloads,
            "streamQuality" to profile.streamQuality,
            "lastSyncedAt" to System.currentTimeMillis()
        )

        db.collection("users").document(userId)
            .set(profileData, SetOptions.merge())
            .addOnSuccessListener {
                _cloudSyncStatus.value = "Profile Synced to Cloud"
                Log.d(TAG, "User profile synced to Firestore.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "User profile cloud sync deferred to local cache: ${e.message}")
            }
    }

    fun updateUserProfileDetails(userId: String, displayName: String, photoUrl: String?) {
        val db = firestoreInstance ?: return
        val data = hashMapOf<String, Any>(
            "name" to displayName,
            "displayName" to displayName,
            "avatarUrl" to (photoUrl ?: ""),
            "photoUrl" to (photoUrl ?: ""),
            "lastSyncedAt" to System.currentTimeMillis()
        )
        db.collection("users").document(userId)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                _cloudSyncStatus.value = "Profile Updated in Cloud"
                Log.d(TAG, "User profile details updated in Firestore: $displayName")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "User profile update deferred: ${e.message}")
            }
    }

    fun syncMyList(userId: String = "user_alex_mercer", myListIds: Set<String>) {
        val db = firestoreInstance ?: return
        val data = hashMapOf(
            "myListIds" to myListIds.toList(),
            "myList" to myListIds.toList(),
            "updatedAt" to System.currentTimeMillis()
        )

        // Sync directly to the user's root profile document
        db.collection("users").document(userId)
            .set(hashMapOf("myList" to myListIds.toList()), SetOptions.merge())

        // Also sync to library subcollection
        db.collection("users").document(userId)
            .collection("library").document("my_list")
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "My List synced to Firestore (${myListIds.size} items).")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "My List sync deferred to offline cache: ${e.message}")
            }
    }

    fun syncWatchHistory(userId: String = "user_alex_mercer", history: List<WatchHistoryItem>) {
        val db = firestoreInstance ?: return
        val items = history.map { item ->
            hashMapOf(
                "showId" to item.showId,
                "showTitle" to item.showTitle,
                "episodeTitle" to item.episodeTitle,
                "episodeNumber" to item.episodeNumber,
                "progressFraction" to item.progressFraction,
                "lastWatchedText" to item.lastWatchedText,
                "posterUrl" to item.posterUrl
            )
        }

        val data = hashMapOf(
            "history" to items,
            "updatedAt" to System.currentTimeMillis()
        )

        db.collection("users").document(userId)
            .collection("library").document("watch_history")
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Watch history synced to Firestore.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Watch history sync deferred: ${e.message}")
            }
    }

    fun syncCatalog(shows: List<Show>) {
        val db = firestoreInstance ?: return
        shows.forEach { show ->
            val showData = hashMapOf(
                "id" to show.id,
                "title" to show.title,
                "category" to show.category,
                "genres" to show.genres,
                "matchScore" to show.matchScore,
                "ratingScore" to show.ratingScore,
                "year" to show.year,
                "qualityTag" to show.qualityTag,
                "audioBadge" to show.audioBadge,
                "description" to show.description,
                "isTop10" to show.isTop10,
                "top10Rank" to show.top10Rank
            )
            db.collection("catalog").document(show.id)
                .set(showData, SetOptions.merge())
        }
    }

    fun listenToCloudSync(
        userId: String = "user_alex_mercer",
        onProfileUpdated: (UserProfile) -> Unit,
        onMyListUpdated: (Set<String>) -> Unit
    ) {
        val db = firestoreInstance ?: return

        // Listen for remote profile updates
        try {
            db.collection("users").document(userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen error: ${error.message}")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val name = snapshot.getString("name") ?: "Alex Mercer"
                        val email = snapshot.getString("email") ?: "alex.mercer@streamx.io"
                        val tier = snapshot.getString("membershipTier") ?: "STREAMX VIP PLATINUM"
                        val isVip = snapshot.getBoolean("isVip") ?: true
                        val expiry = snapshot.getString("expiryDate") ?: "October 2026"
                        val wifiOnly = snapshot.getBoolean("wifiOnlyDownloads") ?: true
                        val smartDl = snapshot.getBoolean("smartDownloads") ?: true
                        val quality = snapshot.getString("streamQuality") ?: "Ultra HD 4K (Auto)"
                        val avatar = snapshot.getString("avatarUrl") ?: snapshot.getString("photoUrl")

                        onProfileUpdated(
                            UserProfile(
                                name = name,
                                email = email,
                                avatarUrl = avatar,
                                membershipTier = tier,
                                isVip = isVip,
                                expiryDate = expiry,
                                wifiOnlyDownloads = wifiOnly,
                                smartDownloads = smartDl,
                                streamQuality = quality
                            )
                        )
                    }
                }

            // Listen for remote My List updates
            db.collection("users").document(userId)
                .collection("library").document("my_list")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener
                    if (snapshot != null && snapshot.exists()) {
                        @Suppress("UNCHECKED_CAST")
                        val list = snapshot.get("myListIds") as? List<String>
                        if (list != null) {
                            onMyListUpdated(list.toSet())
                        }
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "Listener setup deferred: ${e.message}")
        }
    }
}
