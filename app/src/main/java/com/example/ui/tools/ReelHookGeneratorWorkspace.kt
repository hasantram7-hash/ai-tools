package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
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

enum class ReelNiche(val label: String, val icon: String) {
    PRODUCTIVITY("Productivity & Tech", "⚡"),
    FINANCE("Money & Wealth", "💰"),
    FITNESS("Fitness & Gym", "🔥"),
    STORYTELLING("Story & Mystery", "🎙️"),
    BUSINESS("Startups & Sales", "📈")
}

data class ReelHookScript(
    val hookType: String,
    val hookLine: String,
    val visualCue: String,
    val bodyFramework: String,
    val callToAction: String
)

@Composable
fun ReelHookGeneratorWorkspace(
    onCopyText: (String) -> Unit
) {
    var topicText by remember { mutableStateOf("") }
    var selectedNiche by remember { mutableStateOf(ReelNiche.PRODUCTIVITY) }
    var generatedHooks by remember { mutableStateOf<List<ReelHookScript>>(emptyList()) }

    fun generateHooks() {
        val topic = topicText.trim().ifEmpty {
            when (selectedNiche) {
                ReelNiche.PRODUCTIVITY -> "how to stop procrastinating using the 2-minute rule"
                ReelNiche.FINANCE -> "why saving in a normal bank account is costing you money"
                ReelNiche.FITNESS -> "the one mistake ruining your muscle recovery"
                ReelNiche.STORYTELLING -> "the craziest psychological experiment you never learned in school"
                ReelNiche.BUSINESS -> "how a solo founder made $10,000 without hiring anyone"
            }
        }

        generatedHooks = listOf(
            ReelHookScript(
                hookType = "Pattern Interrupt (0-3s)",
                hookLine = "\"If you're still doing $topic the old way, stop scrolling right now.\"",
                visualCue = "Point directly at camera, rapid zoom in, bold neon text caption on screen.",
                bodyFramework = "3 Bullet Points: 1. The hidden flaw everyone ignores. 2. The replacement method. 3. Immediate proof/metric.",
                callToAction = "\"Comment 'GUIDE' and I'll DM you the breakdown!\""
            ),
            ReelHookScript(
                hookType = "Curiosity Gap / Secret (0-3s)",
                hookLine = "\"Nobody in $selectedNiche wants to admit this truth about $topic...\"",
                visualCue = "Whispering gesture or holding phone close, fast b-roll cut.",
                bodyFramework = "Story arc: 'I wasted 6 months doing it wrong until I realized this simple shift...'",
                callToAction = "\"Share this with someone who needs to hear this today.\""
            ),
            ReelHookScript(
                hookType = "Contrarian / Unpopular Opinion (0-3s)",
                hookLine = "\"Most advice on $topic is actually complete nonsense. Here’s why.\"",
                visualCue = "Shake head, hold coffee or prop, split screen with proof document.",
                bodyFramework = "Dissect common myth -> Reveal high-leverage alternative -> Step-by-step 15-second demonstration.",
                callToAction = "\"Save this Reel before algorithm buries it!\""
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Topic Input Card
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
                    text = "What is your Reel / Short topic?",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                OutlinedTextField(
                    value = topicText,
                    onValueChange = { topicText = it },
                    placeholder = { Text("e.g. morning routines, crypto investing, workout habits...") },
                    modifier = Modifier.fillMaxWidth().testTag("reel_topic_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Text("Select Creator Niche", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ReelNiche.entries.forEach { niche ->
                        val isSel = selectedNiche == niche
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) AccentPurple else SurfaceContainerHighest)
                                .clickable { selectedNiche = niche }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = niche.icon, fontSize = 18.sp)
                        }
                    }
                }

                Button(
                    onClick = { generateHooks() },
                    modifier = Modifier.fillMaxWidth().testTag("generate_hooks_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Generate Viral 3-Second Hooks", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        // Generated Hooks Output
        if (generatedHooks.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                generatedHooks.forEachIndexed { index, item ->
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
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = item.hookType,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = AccentCyan
                                    )
                                }

                                Button(
                                    onClick = {
                                        val script = "🎬 REEL HOOK: ${item.hookLine}\n\n🎥 VISUAL: ${item.visualCue}\n\n📝 SCRIPT FLOW: ${item.bodyFramework}\n\n👉 CTA: ${item.callToAction}"
                                        onCopyText(script)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SurfaceContainerHighest,
                                        contentColor = TextPrimary
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy Script", style = MaterialTheme.typography.labelSmall)
                                }
                            }

                            // Hook Line
                            Text(
                                text = item.hookLine,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = TextPrimary
                            )

                            // Visual Cue pill
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SurfaceContainerHigh.copy(alpha = 0.6f)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("👁️", fontSize = 14.sp)
                                    Text(
                                        text = "Visual Cue: ${item.visualCue}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                            }

                            // CTA
                            Text(
                                text = "CTA: ${item.callToAction}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentPink
                            )
                        }
                    }
                }
            }
        }
    }
}
