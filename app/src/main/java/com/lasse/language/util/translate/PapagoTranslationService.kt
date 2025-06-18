package com.lasse.language.util.translate

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

data class PapagoTranslationResponse(
    val message: PapagoMessage?
)

data class PapagoMessage(
    val result: PapagoResult?
)

data class PapagoResult(
    val translatedText: String?,
    val srcLangType: String?,
    val tarLangType: String?
)

interface PapagoTranslationService {
    @FormUrlEncoded
    @POST("translation")
    suspend fun translate(
        @Header("x-ncp-apigw-api-key-id") clientId: String,
        @Header("x-ncp-apigw-api-key") clientSecret: String,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String
    ): PapagoTranslationResponse

    companion object {
        private const val BASE_URL = "https://papago.apigw.ntruss.com/nmt/v1/"

        private val retrofit: Retrofit by lazy {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val api: PapagoTranslationService by lazy {
            retrofit.create(PapagoTranslationService::class.java)
        }
    }
} 