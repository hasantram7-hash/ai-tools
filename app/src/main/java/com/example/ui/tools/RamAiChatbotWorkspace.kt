package com.example.ui.tools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.GeminiClient
import com.example.ai.GeminiContent
import com.example.ai.GeminiPart
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String = "Now"
)

@Composable
fun RamAiChatbotWorkspace(
    onNavigateToTool: ((String) -> Unit)? = null,
    onCopyText: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "welcome_1",
                isUser = false,
                text = "Hello! I am Ram AI Assistant 🤖✨\n\nI am your dedicated guide for all 22+ tools in Collection of Ram. You can ask me how any tool works (such as Fake Tweet Maker, BMI Health, Sleep Cycles, Water Tracker, Bio Fonts, or Reel Hooks), get creator advice, or discover the best tool for your daily task!"
            )
        )
    }

    val samplePrompts = listOf(
        "How do I use the Sleep Cycle calculator?",
        "How can I calculate my healthy BMI?",
        "How to create a viral Reel hook?",
        "Which tool is best for daily hydration?"
    )

    fun sendUserMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty() || isLoading) return

        val userMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            isUser = true,
            text = trimmed
        )
        messages.add(userMsg)
        inputMessage = ""
        isLoading = true

        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)

            // Convert chat history to Gemini format
            val history = messages.dropLast(1).map {
                GeminiContent(
                    role = if (it.isUser) "user" else "model",
                    parts = listOf(GeminiPart(text = it.text))
                )
            }

            val result = GeminiClient.sendMessage(history, trimmed)

            isLoading = false
            result.onSuccess { responseText ->
                messages.add(
                    ChatMessage(
                        id = "ai_${System.currentTimeMillis()}",
                        isUser = false,
                        text = responseText
                    )
                )
                listState.animateScrollToItem(messages.size - 1)
            }.onFailure { err ->
                // Fallback smart offline response if API key is missing or errored
                val fallbackReply = generateSmartFallback(trimmed)
                messages.add(
                    ChatMessage(
                        id = "ai_fallback_${System.currentTimeMillis()}",
                        isUser = false,
                        text = fallbackReply
                    )
                )
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 450.dp, max = 650.dp)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Chat Header with status badge
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AccentPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🤖", fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = "Ram AI Assistant",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "App & Feature Specialist • gemini-3.5-flash",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = AccentCyan.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AccentCyan)
                        )
                        Text("Online", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = AccentCyan)
                    }
                }
            }
        }

        // Quick suggestions carousel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            samplePrompts.take(2).forEach { prompt ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { sendUserMessage(prompt) }
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextPrimary,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Message Thread (Scrollable)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.35f)),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubble(message = msg, onCopy = onCopyText)
                }

                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = AccentPurple)
                            Text("Ram AI is thinking...", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                    }
                }
            }
        }

        // Input text bar & Send button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                placeholder = { Text("Ask anything about app tools & features...") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(14.dp)
            )

            IconButton(
                onClick = { sendUserMessage(inputMessage) },
                enabled = inputMessage.isNotBlank() && !isLoading,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (inputMessage.isNotBlank()) AccentPurple else SurfaceContainerHighest)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (inputMessage.isNotBlank()) Color.White else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, onCopy: (String) -> Unit) {
    val isUser = message.isUser
    val bubbleColor = if (isUser) AccentPurple else SurfaceContainerLow
    val textColor = if (isUser) Color.White else TextPrimary
    val align = if (isUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            color = bubbleColor,
            border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f)) else null,
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clickable { onCopy(message.text) }
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.5.sp, lineHeight = 21.sp),
                    color = textColor
                )
            }
        }
    }
}

// Fallback logic in case of network issues or missing API key
private fun generateSmartFallback(query: String): String {
    val lower = query.lowercase()
    return when {
        lower.contains("bmi") || lower.contains("weight") || lower.contains("health") -> {
            "You can use the 'BMI Health & Ideal Weight' tool! Simply enter your height in cm and weight in kg to instantly receive your BMI score, healthy weight target range, and personalized lifestyle guidance."
        }
        lower.contains("water") || lower.contains("drink") || lower.contains("hydrate") -> {
            "Check out the 'Daily Water Hydration Tracker'! It lets you log water intake by glass (250ml), mug (350ml), or bottle (500ml), tracking your progress toward your recommended 3,000ml daily target."
        }
        lower.contains("sleep") || lower.contains("wake") || lower.contains("alarm") -> {
            "Use the 'Sleep Cycle & REM Calculator'! It uses natural 90-minute human REM cycles to calculate the exact optimal times to wake up or sleep so you feel energized and alert without morning grogginess."
        }
        lower.contains("pomodoro") || lower.contains("focus") || lower.contains("study") || lower.contains("timer") -> {
            "The 'Pomodoro Focus Timer' is ideal for productivity sprints. It provides 25-minute deep focus sessions with 5-minute restorative breaks to boost task efficiency."
        }
        lower.contains("tweet") || lower.contains("meme") -> {
            "You can use the 'Fake Tweet & Meme Maker'! Customize display names, @handle, verified blue badge, live likes/reposts counts, and custom quotes with a 1-tap copy feature."
        }
        lower.contains("reel") || lower.contains("hook") || lower.contains("viral") -> {
            "For viral short-form content, open 'Viral Reel & Hook AI'! Pick your niche (Productivity, Finance, Fitness) to generate 0-3 second pattern-interrupt hooks, visual cues, and 30-second script frameworks."
        }
        lower.contains("font") || lower.contains("bio") || lower.contains("insta") -> {
            "Use 'Aesthetic Bio & Fonts' for stylish social media text! It instantly converts text into Gothic, Cursive, Small Caps, and Bubble styles that you can copy directly into your Instagram bio or WhatsApp."
        }
        lower.contains("wheel") || lower.contains("spin") || lower.contains("decision") -> {
            "The 'Spin Wheel Decision' tool makes decision-making effortless and fun (e.g. what to eat or watch). Customize your options and spin the physics-animated wheel!"
        }
        lower.contains("background") || lower.contains("photo") || lower.contains("bg") -> {
            "Try 'Photo BG Remover'! It is a client-side tool to isolate image edges and swap backgrounds with transparent, studio, or custom solid backdrops with zero server upload."
        }
        lower.contains("love") -> {
            "The 'Love Compatibility Calculator' calculates cosmic resonance and zodiac synergy between two names with fun relationship advice and a shareable card."
        }
        else -> {
            "Collection of Ram includes 22+ powerful everyday utilities including: BMI Health Calculator, Water Tracker, Sleep Cycle Calculator, Pomodoro Focus Timer, Fake Tweet Maker, Aesthetic Bio Fonts, Reel Hooks AI, and QR Matrix. Feel free to ask about any specific tool!"
        }
    }
}
