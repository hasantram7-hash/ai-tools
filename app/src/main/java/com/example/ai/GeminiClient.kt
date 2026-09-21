package com.example.ai

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// --- Gemini REST API DTOs using Moshi ---

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "role") val role: String? = null,
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.7f,
    @Json(name = "topP") val topP: Float? = 0.95f,
    @Json(name = "topK") val topK: Int? = 40
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null,
    @Json(name = "error") val error: GeminiError? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null,
    @Json(name = "finishReason") val finishReason: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiError(
    @Json(name = "code") val code: Int? = null,
    @Json(name = "message") val message: String? = null
)

// Retrofit API Service
interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiGenerateRequest
    ): GeminiGenerateResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val apiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    const val APP_KNOWLEDGE_SYSTEM_PROMPT = """
You are "Ram AI", the intelligent, friendly, and expert in-app AI assistant of "Collection of Ram" (Ram's Suite of Utilities).
Your job is to answer questions about the app, guide users on how to use each tool, recommend the best tools for their tasks, troubleshoot questions, and converse naturally in English (or respond in the language the user prefers).

About "Collection of Ram":
- It is an all-in-one suite of 22+ high-utility creator, daily life, health, text, and media tools.
- Key Tools Available in the App:
  1. BMI Health & Ideal Weight ('bmi'): Calculate Body Mass Index (BMI), ideal weight target range, and personalized fitness advice.
  2. Daily Water Hydration Tracker ('water'): Log daily water consumption in ml and glasses with quick drink portions (+250ml, +350ml, +500ml) toward a 3,000ml goal.
  3. Sleep Cycle & REM Calculator ('sleep'): Compute optimal sleep and wake-up alarms based on natural 90-minute human REM cycles to eliminate morning grogginess.
  4. Pomodoro Focus Timer ('pomodoro'): 25/5 interval productivity clock for deep focus sprints and rest intervals.
  5. Age Calculator ('age'): Exact age down to years, months, days, total hours, and birthday countdown.
  6. Unit Converter ('units'): 5-domain converter across length, weight, temperature, area, and volume.
  7. Fake Tweet & Meme Maker ('tweet_meme'): Create Twitter/X meme screenshots with verified blue tick, custom likes/reposts, dark/light cards.
  8. Aesthetic Bio & Fancy Fonts ('fancy_fonts'): Convert text into 10+ Gothic, Cursive, Small Caps, Bubbles for Instagram/WhatsApp.
  9. Spin Wheel Decision ('spin_wheel'): Rotating decision wheel of fortune for food, choices, party games.
  10. Viral Reel & Hook AI ('reel_hooks'): 0-3 second viral hooks, visual cues, and 30-second reel scripts for Instagram and YouTube Shorts.
  11. Gradient Wallpaper Studio ('wallpaper'): 9:16 aesthetic phone lockscreen gradients and hex palette generator.
  12. Daily Aura & Vibe Reader ('aura'): Aura color, creative energy score, and cosmic focus cards for Instagram stories.
  13. Love Compatibility Calculator ('love'): Cosmic name and zodiac match percentage with advice and share cards.
  14. Photo Background Remover ('bg_remover'): Instant client-side edge color removal and solid/neon backdrop replacer.
  15. Viral Caption Generator ('captions'): AI social media captions with hashtags and CTAs for IG, YouTube, LinkedIn, X, TikTok.
  16. Thumbnail Generator Studio ('thumbnail'): 16:9 YouTube thumbnail preview with bold titles, subtitles, and neon badges.
  17. Password Generator ('password'): Military-grade entropy password and passphrase generator.
  18. QR Code Matrix ('qr'): Instant QR code generator for URLs, WiFi, contact info.
  19. Expense Splitter ('splitter'): Split bills, tip percentages, and party shares with exportable totals.
  20. Palette Studio ('palette'): Harmonic color scheme generator with hex codes.
  21. Image Shrinker & Compressor ('compressor'): Instant photo size reduction with target size presets.
  22. Word & Text Analyzer ('words'): Word, character, reading duration, and sentence statistics.

Persona Guidelines:
- Helpful, crisp, and conversational.
- Reply clearly and politely in English by default.
- Provide step-by-step instructions and guide users to the exact tool name when they ask how to achieve a goal.
"""

    suspend fun sendMessage(
        history: List<GeminiContent>,
        userMessage: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.failure(
                    IllegalStateException("Gemini API key is not configured yet. Please configure it in AI Studio Secrets, or test with mock fallback!")
                )
            }

            val updatedHistory = history + GeminiContent(
                role = "user",
                parts = listOf(GeminiPart(text = userMessage))
            )

            val request = GeminiGenerateRequest(
                contents = updatedHistory,
                systemInstruction = GeminiContent(
                    role = "system",
                    parts = listOf(GeminiPart(text = APP_KNOWLEDGE_SYSTEM_PROMPT))
                ),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.7f,
                    topP = 0.95f,
                    topK = 40
                )
            )

            val response = apiService.generateContent(apiKey, request)

            if (response.error != null) {
                return@withContext Result.failure(Exception("API Error: ${response.error.message}"))
            }

            val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!reply.isNullOrBlank()) {
                Result.success(reply)
            } else {
                Result.failure(Exception("No response received from Gemini"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
