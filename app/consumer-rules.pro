# PapagoTranslationService 관련 클래스들 보존
-keep class com.lasse.language.util.transate.PapagoTranslationService { *; }
-keep class com.lasse.language.util.transate.PapagoTranslationResponse { *; }
-keep class com.lasse.language.util.transate.PapagoMessage { *; }
-keep class com.lasse.language.util.transate.PapagoResult { *; }

# PapagoTextView 관련 클래스들 보존
-keep class com.lasse.language.util.transate.PapagoTextView { *; }

# PapagoTranslationCache 관련 클래스들 보존
-keep class com.lasse.language.util.transate.PapagoTranslationCache { *; }
-keep class com.lasse.language.util.transate.PapagoTranslationCache$TranslationResult { *; }
-keep class com.lasse.language.util.transate.PapagoTranslationCache$CacheData { *; }

# Retrofit 관련 클래스들 보존
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp 관련 클래스들 보존
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Gson 관련 클래스들 보존
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer 