package com.lasse.language.util.translate

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.ConfigurationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

// 같은 패키지의 다른 클래스들을 명시적으로 import
import com.lasse.language.util.translate.PapagoTranslationCache
import com.lasse.language.util.translate.PapagoTranslationService

class PapagoTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val TAG = "PapagoTextView"
    private var originalText: String = ""
    private var isSettingTranslatedText = false
    private var sourceLanguage: String = "ko"
    private var targetLanguage: String = "en"
    private var isCacheReady = false
    private var isAutoTranslateMode = true // 자동번역 모드 (기본값 true)
    private var systemLanguage: String = "ko" // 시스템 언어

    companion object {
        private var clientId: String? = null
        private var clientSecret: String? = null
        private var isCacheInitialized = false
        private var defaultAutoTranslateMode = true // 기본 자동번역 모드 설정

        fun initialize(clientId: String, clientSecret: String, autoTranslateMode: Boolean = true) {
            this.clientId = clientId
            this.clientSecret = clientSecret
            this.defaultAutoTranslateMode = autoTranslateMode
            Log.d("PapagoTextView", "API keys initialized with auto-translate mode: $autoTranslateMode")
        }

        // 전역 자동번역 모드 설정 변경
        fun setGlobalAutoTranslateMode(enabled: Boolean) {
            defaultAutoTranslateMode = enabled
            Log.d("PapagoTextView", "Global auto-translate mode changed to: $enabled")
        }

        // 현재 전역 자동번역 모드 설정 확인
        fun getGlobalAutoTranslateMode(): Boolean {
            return defaultAutoTranslateMode
        }

        // API 키 설정 상태 확인
        fun isInitialized(): Boolean {
            return clientId != null && clientSecret != null
        }
    }

    init {
        Log.d(TAG, "PapagoTextView constructor started")
        try {
            initializeCache()
            initializeSystemLanguage()
            // 전역 설정에 따라 자동번역 모드 설정
            isAutoTranslateMode = defaultAutoTranslateMode
            Log.d(TAG, "Auto-translate mode set to: $isAutoTranslateMode")
        } catch (e: Exception) {
            Log.e(TAG, "Initialization failed", e)
        }
        Log.d(TAG, "PapagoTextView constructor completed")
    }

    private fun initializeSystemLanguage() {
        try {
            // 방법 1: Android Configuration에서 직접 가져오기 (가장 정확함)
            val locale = getSystemLocale()
            systemLanguage = locale.language
            
            Log.d(TAG, "System language detected: $systemLanguage")
            Log.d(TAG, "Full locale: ${locale.toString()}")
            Log.d(TAG, "Country: ${locale.country}")
            Log.d(TAG, "Display language: ${locale.displayLanguage}")
            
            // 자동번역 모드가 활성화되어 있다면 타겟 언어를 시스템 언어로 설정
            if (isAutoTranslateMode) {
                targetLanguage = systemLanguage
                Log.d(TAG, "Auto-translate mode: target language set to $systemLanguage")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to detect system language", e)
            // 폴백으로 Locale.getDefault() 사용
            try {
                systemLanguage = Locale.getDefault().language
                Log.w(TAG, "Using fallback system language: $systemLanguage")
            } catch (e2: Exception) {
                systemLanguage = "ko" // 최종 기본값
                Log.w(TAG, "Using default language: $systemLanguage")
            }
        }
    }

    private fun getSystemLocale(): Locale {
        // Android의 시스템 Configuration에서 언어 설정을 가져오는 여러 방법 시도
        return try {
            // 방법 1: Resources.getSystem()을 사용 (가장 정확)
            val systemConfig = Resources.getSystem().configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                systemConfig.locales[0]
            } else {
                @Suppress("DEPRECATION")
                systemConfig.locale
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get system locale from Resources.getSystem(), trying alternative methods", e)
            try {
                // 방법 2: Context의 resources 사용
                val contextConfig = context.resources.configuration
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contextConfig.locales[0]
                } else {
                    @Suppress("DEPRECATION")
                    contextConfig.locale
                }
            } catch (e2: Exception) {
                Log.w(TAG, "Failed to get locale from context resources, using Locale.getDefault()", e2)
                // 방법 3: 마지막 폴백
                Locale.getDefault()
            }
        }
    }

    // 시스템 언어 변경을 감지하고 반영하는 메소드
    fun refreshSystemLanguage() {
        val oldLanguage = systemLanguage
        initializeSystemLanguage()
        
        if (oldLanguage != systemLanguage) {
            Log.d(TAG, "System language changed: $oldLanguage -> $systemLanguage")
            if (isAutoTranslateMode) {
                targetLanguage = systemLanguage
                // 현재 텍스트를 새로운 언어로 다시 번역
                if (originalText.isNotEmpty()) {
                    translateText()
                }
            }
        }
    }

    private fun initializeCache() {
        try {
            if (!isCacheInitialized) {
                val appContext = context.applicationContext
                if (appContext != null) {
                    PapagoTranslationCache.initialize(appContext)
                    isCacheInitialized = true
                    isCacheReady = true
                    Log.d(TAG, "Cache initialization successful")
                } else {
                    Log.w(TAG, "Application context is null")
                }
            } else {
                isCacheReady = true
                Log.d(TAG, "Cache already initialized")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Cache initialization failed", e)
            isCacheReady = false
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.d(TAG, "onFinishInflate started")
        try {
            originalText = text?.toString() ?: ""
            Log.d(TAG, "Initial text: $originalText")
            
            if (originalText.isNotEmpty()) {
                if (!isCacheReady) {
                    initializeCache()
                }
                translateText()
            }
        } catch (e: Exception) {
            Log.e(TAG, "onFinishInflate failed", e)
        }
        Log.d(TAG, "onFinishInflate completed")
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (isSettingTranslatedText) {
            super.setText(text, type)
            return
        }

        try {
            val newText = text?.toString() ?: ""
            
            if (newText == originalText) {
                return
            }
            
            originalText = newText
            Log.d(TAG, "setText called: $originalText")
            
            if (originalText.isEmpty()) {
                super.setText(text, type)
                return
            }
            
            if (!isCacheReady) {
                initializeCache()
            }
            
            translateText()
        } catch (e: Exception) {
            Log.e(TAG, "setText failed", e)
            super.setText(text, type)
        }
    }

    // 자동번역 모드 설정/해제
    fun setAutoTranslateMode(enabled: Boolean) {
        isAutoTranslateMode = enabled
        Log.d(TAG, "Auto-translate mode: $enabled")
        
        if (enabled) {
            // 자동번역 모드 활성화: 소스는 자동감지, 타겟은 시스템 언어
            sourceLanguage = "auto"
            targetLanguage = systemLanguage
            Log.d(TAG, "Auto-translate enabled: auto -> $systemLanguage")
        }
        
        // 현재 텍스트가 있다면 다시 번역
        if (originalText.isNotEmpty()) {
            translateText()
        }
    }

    // 자동번역 모드 상태 확인
    fun isAutoTranslateModeEnabled(): Boolean {
        return isAutoTranslateMode
    }

    // 수동 언어 설정 (자동번역 모드 비활성화)
    fun setLanguages(source: String, target: String) {
        isAutoTranslateMode = false
        sourceLanguage = source
        targetLanguage = target
        Log.d(TAG, "Manual language setting: $source -> $target")
        
        if (originalText.isNotEmpty()) {
            translateText()
        }
    }

    // 강제 번역 (캐시 무시)
    fun forceTranslate() {
        Log.d(TAG, "Force translate executed")
        if (originalText.isNotEmpty()) {
            performTranslation()
        }
    }

    private fun translateText() {
        try {
            if (originalText.isBlank()) {
                Log.d(TAG, "Empty text, skipping translation")
                super.setText(originalText, BufferType.NORMAL)
                return
            }

            if (clientId == null || clientSecret == null) {
                Log.e(TAG, "Papago API keys not set")
                Toast.makeText(context, "Please set Papago API keys first", Toast.LENGTH_SHORT).show()
                super.setText(originalText, BufferType.NORMAL)
                return
            }

            // 자동번역 모드에서 언어 체크 (API 호출 전에 미리 확인)
            if (isAutoTranslateMode) {
                val detectedLanguage = detectTextLanguage(originalText)
                
                // 감지된 언어와 시스템 언어가 같으면 번역하지 않음
                if (detectedLanguage == systemLanguage) {
                    Log.d(TAG, "Text language ($detectedLanguage) matches system language ($systemLanguage), no translation needed")
                    isSettingTranslatedText = true
                    super.setText(originalText, BufferType.NORMAL)
                    isSettingTranslatedText = false
                    Toast.makeText(context, "Same language: $originalText", Toast.LENGTH_SHORT).show()
                    return
                }
                
                Log.d(TAG, "Detected language: $detectedLanguage, System language: $systemLanguage, translation needed")
            }

            // 캐시 확인
            if (isCacheReady) {
                val cacheKey = if (isAutoTranslateMode) {
                    // 자동번역 모드에서는 감지된 언어를 캐시 키로 사용
                    val detectedLang = detectTextLanguage(originalText)
                    detectedLang ?: "auto"
                } else {
                    sourceLanguage
                }
                
                val cachedTranslation = PapagoTranslationCache.getCachedTranslation(
                    originalText, cacheKey, targetLanguage
                )

                if (cachedTranslation != null) {
                    Log.d(TAG, "Using cached translation ($cacheKey->$targetLanguage): $originalText -> $cachedTranslation")
                    isSettingTranslatedText = true
                    super.setText(cachedTranslation, BufferType.NORMAL)
                    isSettingTranslatedText = false
                    Toast.makeText(context, "Cached: $cachedTranslation", Toast.LENGTH_SHORT).show()
                    return
                }
                
                Log.d(TAG, "No cache found for key: $cacheKey->$targetLanguage")
            }

            Log.d(TAG, "No cache found, calling API")
            super.setText(originalText, BufferType.NORMAL)
            performTranslation()
            
        } catch (e: Exception) {
            Log.e(TAG, "translateText failed", e)
            super.setText(originalText, BufferType.NORMAL)
        }
    }

    private fun detectTextLanguage(text: String): String? {
        // 더 정확한 언어 감지 로직
        val koreanCount = text.count { it in '\uAC00'..'\uD7AF' } // 한글
        val japaneseCount = text.count { 
            it in '\u3040'..'\u309F' || it in '\u30A0'..'\u30FF' || it in '\u4E00'..'\u9FAF' 
        } // 히라가나, 카타카나, 한자
        val englishCount = text.count { it.isLetter() && it.code < 128 } // ASCII 영문
        val totalLetters = text.count { it.isLetter() }
        
        if (totalLetters == 0) return null
        
        // 각 언어의 비율 계산
        val koreanRatio = koreanCount.toFloat() / totalLetters
        val japaneseRatio = japaneseCount.toFloat() / totalLetters
        val englishRatio = englishCount.toFloat() / totalLetters
        
        Log.d(TAG, "Language detection - Korean: $koreanRatio, Japanese: $japaneseRatio, English: $englishRatio")
        
        // 30% 이상이면 해당 언어로 판단
        return when {
            koreanRatio >= 0.3 -> {
                Log.d(TAG, "Detected as Korean (ratio: $koreanRatio)")
                "ko"
            }
            japaneseRatio >= 0.3 -> {
                Log.d(TAG, "Detected as Japanese (ratio: $japaneseRatio)")
                "ja"
            }
            englishRatio >= 0.5 -> {
                Log.d(TAG, "Detected as English (ratio: $englishRatio)")
                "en"
            }
            else -> {
                Log.d(TAG, "Could not determine language confidently")
                null
            }
        }
    }

    private fun isTextInSystemLanguage(text: String): Boolean {
        // 이 함수는 이제 detectTextLanguage로 대체됨 (하위 호환성을 위해 유지)
        val detectedLang = detectTextLanguage(text)
        return detectedLang == systemLanguage
    }

    private fun performTranslation() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "Papago translation started")
                Log.d(TAG, "Original text: $originalText")
                Log.d(TAG, "Translation: $sourceLanguage -> $targetLanguage")
                Log.d(TAG, "Auto-translate mode: $isAutoTranslateMode")

                val response = PapagoTranslationService.api.translate(
                    clientId = clientId!!,
                    clientSecret = clientSecret!!,
                    source = sourceLanguage,
                    target = targetLanguage,
                    text = originalText
                )

                val translatedText = response.message?.result?.translatedText
                val detectedSourceLang = response.message?.result?.srcLangType
                val detectedTargetLang = response.message?.result?.tarLangType

                withContext(Dispatchers.Main) {
                    if (!translatedText.isNullOrEmpty()) {
                        Log.d(TAG, "Translation successful")
                        Log.d(TAG, "Detected source: $detectedSourceLang")
                        Log.d(TAG, "Target language: $detectedTargetLang")
                        Log.d(TAG, "Translation result: $translatedText")
                        
                        // 자동번역 모드에서 같은 언어 체크
                        if (isAutoTranslateMode && detectedSourceLang == targetLanguage) {
                            Log.d(TAG, "Source and target languages are the same, using original text")
                            isSettingTranslatedText = true
                            super.setText(originalText, BufferType.NORMAL)
                            isSettingTranslatedText = false
                            Toast.makeText(context, "Same language detected: $originalText", Toast.LENGTH_SHORT).show()
                            return@withContext
                        }
                        
                        // 캐시에 저장
                        if (isCacheReady) {
                            try {
                                val actualSourceLang = if (isAutoTranslateMode) (detectedSourceLang ?: "auto") else sourceLanguage
                                PapagoTranslationCache.cacheTranslation(
                                    originalText = originalText,
                                    translatedText = translatedText,
                                    sourceLanguage = actualSourceLang,
                                    targetLanguage = detectedTargetLang ?: targetLanguage
                                )
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed to cache translation", e)
                            }
                        }
                        
                        isSettingTranslatedText = true
                        super.setText(translatedText, BufferType.NORMAL)
                        isSettingTranslatedText = false
                        
                        val modeText = if (isAutoTranslateMode) "Auto" else "Manual"
                        Toast.makeText(context, "$modeText: $translatedText", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "Translation result is empty")
                        Toast.makeText(context, "Translation result is empty", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Papago translation failed", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Translation failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 시스템 언어 정보 반환
    fun getSystemLanguage(): String {
        return systemLanguage
    }

    // 현재 번역 모드 정보 반환
    fun getTranslationModeInfo(): String {
        return if (isAutoTranslateMode) {
            "Auto-translate: auto -> $systemLanguage"
        } else {
            "Manual: $sourceLanguage -> $targetLanguage"
        }
    }

    // 캐시 관련 유틸리티 메소드들
    fun getCacheSize(): Int {
        return if (isCacheReady) {
            try {
                PapagoTranslationCache.getTotalTranslationCount()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get cache size", e)
                0
            }
        } else {
            0
        }
    }

    fun getCachedTextCount(): Int {
        return if (isCacheReady) {
            try {
                PapagoTranslationCache.getCachedTextCount()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get cached text count", e)
                0
            }
        } else {
            0
        }
    }

    fun getCacheInfo(): String {
        return if (isCacheReady) {
            try {
                PapagoTranslationCache.getCacheInfo()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get cache info", e)
                "Failed to get cache info"
            }
        } else {
            "Cache not initialized"
        }
    }

    fun clearCache() {
        if (isCacheReady) {
            try {
                PapagoTranslationCache.clearCache()
                Log.d(TAG, "Translation cache cleared")
                Toast.makeText(context, "Translation cache cleared", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to clear cache", e)
                Toast.makeText(context, "Failed to clear cache", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Cache not initialized", Toast.LENGTH_SHORT).show()
        }
    }

    fun getTranslationCountForCurrentText(): Int {
        return if (isCacheReady && originalText.isNotEmpty()) {
            try {
                PapagoTranslationCache.getTranslationCountForText(originalText)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get translation count for current text", e)
                0
            }
        } else {
            0
        }
    }

    fun getAllTranslationsForCurrentText(): Map<String, PapagoTranslationCache.TranslationResult>? {
        return if (isCacheReady && originalText.isNotEmpty()) {
            try {
                PapagoTranslationCache.getAllTranslationsForText(originalText)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get all translations for current text", e)
                null
            }
        } else {
            null
        }
    }

    // 전역 설정을 현재 인스턴스에 적용
    fun applyGlobalAutoTranslateMode() {
        val globalMode = defaultAutoTranslateMode
        if (isAutoTranslateMode != globalMode) {
            setAutoTranslateMode(globalMode)
            Log.d(TAG, "Applied global auto-translate mode: $globalMode")
        }
    }

    // 현재 설정 정보 반환
    fun getCurrentModeInfo(): String {
        return """
            Instance mode: ${if (isAutoTranslateMode) "Auto" else "Manual"}
            Global default: ${if (defaultAutoTranslateMode) "Auto" else "Manual"}
            System language: $systemLanguage
            ${if (!isAutoTranslateMode) "Manual setting: $sourceLanguage -> $targetLanguage" else ""}
        """.trimIndent()
    }
} 
