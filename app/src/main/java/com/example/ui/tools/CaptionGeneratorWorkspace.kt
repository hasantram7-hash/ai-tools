package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tag
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
import com.example.ui.theme.*
import kotlin.random.Random

enum class SocialPlatform(val displayName: String, val iconLabel: String) {
    INSTAGRAM("Instagram", "📸"),
    YOUTUBE("YouTube", "▶️"),
    LINKEDIN("LinkedIn", "💼"),
    TWITTER_X("X / Twitter", "🐦"),
    TIKTOK("TikTok", "🎵")
}

enum class CaptionTone(val displayName: String) {
    ENGAGING("Engaging & Catchy"),
    CASUAL("Casual & Relatable"),
    VIRAL_HOOK("Viral Hook / Bold"),
    PROFESSIONAL("Deep & Professional"),
    MINIMALIST("Aesthetic / Short")
}

data class GeneratedCaption(
    val captionText: String,
    val hashtags: List<String>,
    val characterCount: Int
)

@Composable
fun CaptionGeneratorWorkspace(
    onCopyText: (String) -> Unit
) {
    var topicText by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf(SocialPlatform.INSTAGRAM) }
    var selectedTone by remember { mutableStateOf(CaptionTone.ENGAGING) }
    var includeHashtags by remember { mutableStateOf(true) }
    var includeCallToAction by remember { mutableStateOf(true) }

    var generatedList by remember { mutableStateOf<List<GeneratedCaption>>(emptyList()) }

    fun generateCaptions() {
        val topic = topicText.trim().ifEmpty { "productivity tools and daily creative lifestyle" }

        val samples = when (selectedTone) {
            CaptionTone.VIRAL_HOOK -> listOf(
                "Stop scrolling if you care about $topic. 🛑 Here is what almost nobody is discussing: when you master this simple framework, everything changes.",
                "The biggest mistake people make with $topic? Thinking it needs to be complicated. Here is the unvarnished truth you need to hear today:",
                "I tested 20+ methods for $topic so you don’t have to. The outcome blew my expectations away. Save this before you lose it! ⚡"
            )
            CaptionTone.ENGAGING -> listOf(
                "Let’s talk about $topic. ✨ Some days require pure focus, other days require smart systems. Which one is helping you win this week?",
                "Behind the scenes of $topic: it’s rarely about perfection and always about consistent momentum. Drop a ❤️ if you agree!",
                "Quick reminder for anyone diving into $topic: small, deliberate habits beat sudden bursts of motivation every single time."
            )
            CaptionTone.CASUAL -> listOf(
                "Honestly just obsessed with $topic lately. No fancy words, just pure genuine vibes. How are you approaching yours? ☕✨",
                "POV: You finally cracked the code on $topic and suddenly your whole week feels effortless. Manifesting this for you!",
                "Unpopular opinion: $topic is actually 10x more enjoyable when you stop overthinking and just dive in."
            )
            CaptionTone.PROFESSIONAL -> listOf(
                "Reflecting on modern strategies for $topic. Key takeaways from this quarter: prioritize high leverage over brute force, and iterate relentlessly.",
                "In an era of rapid transformation, mastering $topic is no longer optional—it’s a foundational competitive advantage.",
                "A structured overview on how leading practitioners approach $topic with precision, clarity, and measurable return on effort."
            )
            CaptionTone.MINIMALIST -> listOf(
                "Quiet progress in $topic. ✨",
                "Mastery is subtraction, not addition. Current focus: $topic.",
                "Elevating $topic day by day. Notes in bio."
            )
        }

        val tagsPool = listOf(
            "#${topic.replace(" ", "")}",
            "#CreatorEconomy",
            "#ProductivityHacks",
            "#DailyInspiration",
            "#TechTrends",
            "#GrowthMindset",
            "#ModernCreator",
            "#SmartWork"
        ).shuffled().take(if (includeHashtags) 5 else 0)

        val cta = if (includeCallToAction) when (selectedPlatform) {
            SocialPlatform.INSTAGRAM -> "\n\n👉 Save this post & tap the link in bio for the complete breakdown!"
            SocialPlatform.YOUTUBE -> "\n\n🔔 Don't forget to Like, Subscribe & hit the bell icon for weekly deep dives!"
            SocialPlatform.LINKEDIN -> "\n\n💬 What’s your experience with this? Join the conversation in the comments below."
            SocialPlatform.TWITTER_X -> "\n\n🔁 Retweet if this resonated with you and bookmark for later."
            SocialPlatform.TIKTOK -> "\n\n✨ Follow for daily tips & share with a friend who needs to see this!"
        } else ""

        val list = samples.map { body ->
            val full = body + cta + (if (tagsPool.isNotEmpty()) "\n\n" + tagsPool.joinToString(" ") else "")
            GeneratedCaption(
                captionText = full,
                hashtags = tagsPool,
                characterCount = full.length
            )
        }

        generatedList = list
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Controls Input Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Describe your post or topic:", style = MaterialTheme.typography.titleMedium, color = TextPrimary)

                OutlinedTextField(
                    value = topicText,
                    onValueChange = { topicText = it },
                    placeholder = { Text("e.g. morning routine for tech founders, budget travel tips, workout motivation...") },
                    modifier = Modifier.fillMaxWidth().testTag("caption_topic_input"),
                    singleLine = false,
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = BorderOutline,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Platform selection
                Text("Target Platform", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SocialPlatform.entries.forEach { p ->
                        val isSel = selectedPlatform == p
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) AccentPurple else SurfaceContainerHighest)
                                .clickable { selectedPlatform = p }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = p.iconLabel,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                // Tone selection
                Text("Vibe & Tone", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CaptionTone.entries.take(3).forEach { tone ->
                        val isSel = selectedTone == tone
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) AccentCyan else SurfaceContainerHighest)
                                .clickable { selectedTone = tone }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tone.displayName.split(" ").first(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSel) Color(0xFF003840) else TextSecondary
                            )
                        }
                    }
                }

                // Toggles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = includeHashtags,
                            onCheckedChange = { includeHashtags = it },
                            colors = CheckboxDefaults.colors(checkedColor = AccentPurple)
                        )
                        Text("Add Hashtags", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = includeCallToAction,
                            onCheckedChange = { includeCallToAction = it },
                            colors = CheckboxDefaults.colors(checkedColor = AccentPurple)
                        )
                        Text("Include CTA", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }
                }

                Button(
                    onClick = { generateCaptions() },
                    modifier = Modifier.fillMaxWidth().testTag("generate_captions_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Generate Viral Captions", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        // Generated Captions Feed
        if (generatedList.isNotEmpty()) {
            Text(
                text = "Instant Generated Variations (${selectedPlatform.displayName}):",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AccentCyan
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                generatedList.forEachIndexed { index, item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Option ${index + 1} • ${item.characterCount} chars",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = JetBrainsMonoFamily,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = AccentPurple
                                )

                                Button(
                                    onClick = { onCopyText(item.captionText) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SurfaceContainerHighest,
                                        contentColor = TextPrimary
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy", style = MaterialTheme.typography.labelSmall)
                                }
                            }

                            Text(
                                text = item.captionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
