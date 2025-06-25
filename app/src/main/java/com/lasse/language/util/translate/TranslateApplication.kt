package com.lasse.language.util.translate

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

class TranslateApplication : Application() {
    
    companion object {
        private const val TAG = "TranslateApplication"
        private const val META_DATA_CLIENT_ID = "papago_client_id"
        private const val META_DATA_CLIENT_SECRET = "papago_client_secret"
        private const val META_DATA_AUTO_TRANSLATE_MODE = "papago_auto_translate_mode"
    }
    
    override fun onCreate() {
        super.onCreate()
        initializePapagoTextView()
    }
    
    private fun initializePapagoTextView() {
        try {
            val clientId = getMetaDataValue(META_DATA_CLIENT_ID)
            val clientSecret = getMetaDataValue(META_DATA_CLIENT_SECRET)
            val autoTranslateMode = getMetaDataBooleanValue(META_DATA_AUTO_TRANSLATE_MODE, true)
            
            if (clientId != null && clientSecret != null) {
                PapagoTextView.initialize(clientId, clientSecret, autoTranslateMode)
                Log.d(TAG, "PapagoTextView initialized successfully with auto-translate mode: $autoTranslateMode")
            } else {
                Log.w(TAG, "Papago API credentials not found in AndroidManifest meta-data")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize PapagoTextView", e)
        }
    }
    
    private fun getMetaDataValue(key: String): String? {
        return try {
            val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            ai.metaData?.getString(key)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get meta-data: $key", e)
            null
        }
    }
    
    private fun getMetaDataBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return try {
            val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            ai.metaData?.getBoolean(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get boolean meta-data: $key", e)
            defaultValue
        }
    }
} 