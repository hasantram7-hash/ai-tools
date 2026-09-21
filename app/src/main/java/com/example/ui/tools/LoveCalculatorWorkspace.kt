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
import androidx.compose.material.icons.filled.Favorite
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
import kotlin.math.abs

@Composable
fun LoveCalculatorWorkspace(
    onCopyText: (String) -> Unit
) {
    var name1 by remember { mutableStateOf("") }
    var name2 by remember { mutableStateOf("") }
    var calculatedScore by remember { mutableStateOf<Int?>(null) }
    var calculationTitle by remember { mutableStateOf("") }
    var calculationVerdict by remember { mutableStateOf("") }
    var relationshipAdvice by remember { mutableStateOf("") }

    fun computeMatch() {
        val n1 = name1.trim().lowercase()
        val n2 = name2.trim().lowercase()
        if (n1.isEmpty() || n2.isEmpty()) return

        // Deterministic hashing algorithm combining names for consistent, fun replayability
        val combined = if (n1 < n2) "$n1+$n2" else "$n2+$n1"
        var hash = 0
        for (ch in combined) {
            hash = (hash * 31 + ch.code) and 0x7FFFFFFF
        }
        val score = 55 + (hash % 45) // Score between 55% and 99% for high dopamine

        val (title, verdict, advice) = when {
            score >= 90 -> Triple(
                "Twin Flames & Soulmates 🔥",
                "Legendary resonance! Your energy signatures, humor, and cosmic frequency align almost seamlessly.",
                "Plan an adventurous weekend getaway or launch a creative collaboration together."
            )
            score >= 80 -> Triple(
                "Passionate Electric Synergy ⚡",
                "Deep intellectual sparks and magnetic chemistry. You bring out the boldest aspects in one another.",
                "Dedicate distraction-free date nights to celebrate your joint milestones."
            )
            score >= 70 -> Triple(
                "Harmonious & Grounded 🌱",
                "Balanced, trustworthy, and enduring connection with steady support and mutual respect.",
                "Explore new shared hobbies like cooking, travel, or fitness routines."
            )
            else -> Triple(
                "Playful Dynamic Spark ✨",
                "Vibrant tension with room to discover each other's mysteries and unique worldviews.",
                "Focus on open listening and surprise each other with spontaneous gestures."
            )
        }

        calculatedScore = score
        calculationTitle = title
        calculationVerdict = verdict
        relationshipAdvice = advice
    }

    val animatedProgress by animateFloatAsState(
        targetValue = (calculatedScore ?: 0) / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "love_progress"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Names Input Card
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
                Text(
                    text = "Enter Two Names or Astrological Signs",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                OutlinedTextField(
                    value = name1,
                    onValueChange = { name1 = it },
                    label = { Text("Your Name or Partner A") },
                    placeholder = { Text("e.g. Alex, Maya, Leo...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("love_name1_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPink,
                        unfocusedBorderColor = BorderOutline,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AccentPink.copy(alpha = 0.2f))
                            .border(1.dp, AccentPink.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = AccentPink,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = name2,
                    onValueChange = { name2 = it },
                    label = { Text("Crush, Partner, or Soulmate B") },
                    placeholder = { Text("e.g. Jordan, Sam, Aries...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("love_name2_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPink,
                        unfocusedBorderColor = BorderOutline,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = { computeMatch() },
                    enabled = name1.isNotBlank() && name2.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().testTag("compute_love_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPink,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Calculate Cosmic Compatibility", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        // Result Score Card
        calculatedScore?.let { score ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AccentPink.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Meter circle
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(AccentPink.copy(alpha = 0.25f), Color.Transparent)
                                )
                            )
                            .border(3.dp, AccentPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$score%",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontFamily = JetBrainsMonoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                ),
                                color = AccentPink
                            )
                            Text(
                                text = "MATCH",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = TextMuted
                            )
                        }
                    }

                    Text(
                        text = calculationTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = SoraFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = calculationVerdict,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    // Advice Box
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.5f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "💡", fontSize = 20.sp)
                            Column {
                                Text(
                                    text = "ACTIONABLE ADVICE",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AccentPink
                                )
                                Text(
                                    text = relationshipAdvice,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    // Copy share card
                    Button(
                        onClick = {
                            val shareMessage = "✨ Love Compatibility: ${name1.trim()} & ${name2.trim()} scored $score%! $calculationTitle. Calculated via Collection of Ram."
                            onCopyText(shareMessage)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHighest,
                            contentColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Compatibility Result", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
