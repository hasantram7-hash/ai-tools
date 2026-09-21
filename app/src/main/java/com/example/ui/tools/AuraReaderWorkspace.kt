package com.example.ui.tools

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class AuraProfile(
    val title: String,
    val auraColorHex: Long,
    val gradientEndHex: Long,
    val dominantTrait: String,
    val creativeEnergyScore: Int,
    val vibeSummary: String,
    val cosmicAdvice: String
)

@Composable
fun AuraReaderWorkspace(
    onCopyText: (String) -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var currentMood by remember { mutableStateOf("Focused") }
    var generatedAura by remember { mutableStateOf<AuraProfile?>(null) }

    val moodOptions = listOf("Focused ⚡", "Chill ☕", "Creative 🎨", "Ambitious 🚀", "Mysterious 🌙")

    fun calculateAura() {
        val name = userName.trim().lowercase().ifEmpty { "ram" }
        var hash = 0
        for (c in name + currentMood) {
            hash = (hash * 37 + c.code) and 0x7FFFFFFF
        }

        val auras = listOf(
            AuraProfile(
                title = "Electric Violet Aura 🔮",
                auraColorHex = 0xFF8B5CF6,
                gradientEndHex = 0xFF3B82F6,
                dominantTrait = "High Intuition & Deep Focus",
                creativeEnergyScore = 94,
                vibeSummary = "You are currently emitting high-frequency creative magnetism. Distractions bounce off you effortlessly.",
                cosmicAdvice = "Channel this sprint into high-leverage projects before the day winds down."
            ),
            AuraProfile(
                title = "Neon Cyan Flow 🌊",
                auraColorHex = 0xFF06B6D4,
                gradientEndHex = 0xFF10B981,
                dominantTrait = "Radical Clarity & Strategic Vision",
                creativeEnergyScore = 88,
                vibeSummary = "Cool-headed and lucid. You see three steps ahead while everyone else is getting bogged down in details.",
                cosmicAdvice = "Make key decisions today. Your gut instinct and logical thinking are in rare harmony."
            ),
            AuraProfile(
                title = "Crimson Solar Drive 🔥",
                auraColorHex = 0xFFF43F5E,
                gradientEndHex = 0xFFF59E0B,
                dominantTrait = "Relentless Execution & Fire",
                creativeEnergyScore = 96,
                vibeSummary = "Intense forward momentum. You are ready to shatter bottlenecks and finish pending goals.",
                cosmicAdvice = "Stay hydrated and take 5-minute breathers so you don't burn out your allies."
            ),
            AuraProfile(
                title = "Cosmic Golden Amber ✨",
                auraColorHex = 0xFFF59E0B,
                gradientEndHex = 0xFFEC4899,
                dominantTrait = "Charisma & Uplifting Energy",
                creativeEnergyScore = 91,
                vibeSummary = "Warm, magnetic aura that naturally commands attention and inspires collaborative teamwork.",
                cosmicAdvice = "Reach out to old friends or pitch that creative idea you’ve been sitting on."
            )
        )

        generatedAura = auras[hash % auras.size]
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input Form
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.7f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Discover Your Daily Vibe & Cosmic Energy",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Your Name or Handle") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("aura_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Text("How are you feeling right now?", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    moodOptions.take(4).forEach { mood ->
                        val isSel = currentMood == mood
                        FilterChip(
                            selected = isSel,
                            onClick = { currentMood = mood },
                            label = { Text(mood, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Button(
                    onClick = { calculateAura() },
                    modifier = Modifier.fillMaxWidth().testTag("read_aura_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Read My Daily Aura", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        // Generated Aura Story Card
        generatedAura?.let { aura ->
            val auraColor = Color(aura.auraColorHex)
            val endColor = Color(aura.gradientEndHex)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, auraColor.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Glowing aura sphere
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(auraColor, endColor, Color.Transparent)))
                            .border(3.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${aura.creativeEnergyScore}%",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 32.sp
                            )
                        )
                    }

                    Text(
                        text = aura.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = SoraFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = auraColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "TRAIT: ${aura.dominantTrait.uppercase()}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = auraColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = aura.vibeSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerHigh.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("💡", fontSize = 18.sp)
                            Text(
                                text = aura.cosmicAdvice,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val shareCard = "🔮 My Daily Aura: ${aura.title} (${aura.creativeEnergyScore}% Energy). \"${aura.dominantTrait}\" - Read via Collection of Ram."
                            onCopyText(shareCard)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHighest,
                            contentColor = TextPrimary
                        )
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Aura Card", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
