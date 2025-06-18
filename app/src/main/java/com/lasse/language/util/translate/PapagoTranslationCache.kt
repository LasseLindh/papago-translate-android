package com.lasse.language.util.translate

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PapagoTranslationCache {
    private const val PREFS_NAME = "papago_translation_cache"
    private const val CACHE_KEY = "translations"
    private const val TAG = "PapagoTranslationCache"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()
    
    private val cache = mutableMapOf<String, MutableMap<String, TranslationResult>>()

    data class TranslationResult(
        val translatedText: String,
        val sourceLanguage: String,
        val targetLanguage: String,
        val timestamp: Long
    )

    data class CacheData(
        val translations: Map<String, Map<String, TranslationResult>>
    )

    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadCache()
        Log.d(TAG, "Cache initialized. Text count: ${cache.size}")
        
        var totalCount = 0
        for (translations in cache.values) {
            totalCount += translations.size
        }
        Log.d(TAG, "Total translations: $totalCount")
    }

    private fun loadCache() {
        val json = prefs.getString(CACHE_KEY, null)
        if (json != null) {
            try {
                val type = object : TypeToken<CacheData>() {}.type
                val cacheData = gson.fromJson<CacheData>(json, type)
                
                cacheData.translations.forEach { (originalText, translations) ->
                    cache[originalText] = translations.toMutableMap()
                }
                
                Log.d(TAG, "Cache loaded: ${cache.size} texts")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load cache", e)
                cache.clear()
            }
        }
    }

    private fun saveCache() {
        try {
            val cacheData = CacheData(cache.toMap())
            val json = gson.toJson(cacheData)
            prefs.edit().putString(CACHE_KEY, json).apply()
            
            var totalCount = 0
            for (translations in cache.values) {
                totalCount += translations.size
            }
            Log.d(TAG, "Cache saved: ${cache.size} texts, $totalCount translations")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save cache", e)
        }
    }

    fun getCachedTranslation(
        originalText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String? {
        val languageKey = "$sourceLanguage->$targetLanguage"
        val translationResult = cache[originalText]?.get(languageKey)
        
        return if (translationResult != null) {
            Log.d(TAG, "Cache hit: $originalText ($languageKey) -> ${translationResult.translatedText}")
            translationResult.translatedText
        } else {
            Log.d(TAG, "Cache miss: $originalText ($languageKey)")
            val availableTranslations = cache[originalText]?.keys
            if (!availableTranslations.isNullOrEmpty()) {
                Log.d(TAG, "Available translations: $availableTranslations")
            }
            null
        }
    }

    fun cacheTranslation(
        originalText: String,
        translatedText: String,
        sourceLanguage: String,
        targetLanguage: String
    ) {
        val languageKey = "$sourceLanguage->$targetLanguage"
        val translationResult = TranslationResult(
            translatedText = translatedText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            timestamp = System.currentTimeMillis()
        )
        
        if (!cache.containsKey(originalText)) {
            cache[originalText] = mutableMapOf()
        }
        
        cache[originalText]!![languageKey] = translationResult
        Log.d(TAG, "Translation cached: $originalText ($languageKey) -> $translatedText")

        saveCache()
    }

    fun clearCache() {
        cache.clear()
        prefs.edit().clear().apply()
        Log.d(TAG, "Cache cleared")
    }

    fun getTotalTranslationCount(): Int {
        var count = 0
        for (translations in cache.values) {
            count += translations.size
        }
        return count
    }

    fun getCachedTextCount(): Int {
        return cache.size
    }

    fun getTranslationCountForText(originalText: String): Int {
        return cache[originalText]?.size ?: 0
    }

    fun getAllTranslationsForText(originalText: String): Map<String, TranslationResult>? {
        return cache[originalText]?.toMap()
    }

    fun getCacheInfo(): String {
        val textCount = getCachedTextCount()
        val totalTranslations = getTotalTranslationCount()
        val avgTranslationsPerText = if (textCount > 0) totalTranslations.toFloat() / textCount else 0f
        
        return "Cached texts: $textCount\nTotal translations: $totalTranslations\nAvg per text: %.1f".format(avgTranslationsPerText)
    }

    fun getTranslationCountForLanguagePair(sourceLanguage: String, targetLanguage: String): Int {
        val languageKey = "$sourceLanguage->$targetLanguage"
        var count = 0
        for (translations in cache.values) {
            if (translations.containsKey(languageKey)) {
                count++
            }
        }
        return count
    }

    fun getMostTranslatedTexts(limit: Int = 5): List<Pair<String, Int>> {
        return cache.map { (text, translations) -> text to translations.size }
            .sortedByDescending { it.second }
            .take(limit)
    }
} 