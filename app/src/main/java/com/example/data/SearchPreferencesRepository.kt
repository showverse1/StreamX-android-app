package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

class SearchPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _recentSearches = MutableStateFlow<List<String>>(loadRecentSearches())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    private fun loadRecentSearches(): List<String> {
        val json = prefs.getString(KEY_RECENT_SEARCHES, null) ?: return listOf(
            "Solo Leveling",
            "Queen of Tears",
            "Demon Slayer"
        )
        return try {
            val array = JSONArray(json)
            val list = mutableListOf<String>()
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun persist(list: List<String>) {
        val array = JSONArray()
        list.forEach { array.put(it) }
        prefs.edit().putString(KEY_RECENT_SEARCHES, array.toString()).apply()
        _recentSearches.value = list
    }

    fun addQuery(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return

        val current = _recentSearches.value.toMutableList()
        // Remove existing case-insensitively to bring to top
        current.removeAll { it.equals(trimmed, ignoreCase = true) }
        current.add(0, trimmed)
        val capped = current.take(10)
        persist(capped)
    }

    fun removeQuery(query: String) {
        val current = _recentSearches.value.toMutableList()
        current.removeAll { it.equals(query, ignoreCase = true) }
        persist(current)
    }

    fun clearAll() {
        persist(emptyList())
    }

    companion object {
        private const val PREFS_NAME = "streamx_search_prefs"
        private const val KEY_RECENT_SEARCHES = "recent_queries"
    }
}
