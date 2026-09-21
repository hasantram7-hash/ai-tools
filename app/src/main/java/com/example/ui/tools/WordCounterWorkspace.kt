package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*
import kotlin.math.ceil

@Composable
fun WordCounterWorkspace(
    onCopyText: (String) -> Unit
) {
    var textInput by remember { mutableStateOf("") }

    val (wordCount, charCount, noSpaceCount, sentenceCount, paragraphCount, readingMin, topKeywords) = remember(textInput) {
        val trimmed = textInput.trim()
        val words = if (trimmed.isEmpty()) emptyList() else trimmed.split("\\s+".toRegex())
        val chars = textInput.length
        val noSpace = textInput.replace("\\s".toRegex(), "").length
        val sentences = if (trimmed.isEmpty()) 0 else trimmed.split("[.!?]+".toRegex()).filter { it.isNotBlank() }.size
        val paragraphs = if (trimmed.isEmpty()) 0 else textInput.split("\n+".toRegex()).filter { it.isNotBlank() }.size
        val readTime = if (words.isEmpty()) 0 else ceil(words.size / 220.0).toInt()

        val freqMap = mutableMapOf<String, Int>()
        for (w in words) {
            val clean = w.lowercase().replace("[^a-z0-9]".toRegex(), "")
            if (clean.length > 3) {
                freqMap[clean] = (freqMap[clean] ?: 0) + 1
            }
        }
        val topList = freqMap.entries.sortedByDescending { it.value }.take(4).map { it.key to it.value }

        AnalysisResult(words.size, chars, noSpace, sentences, paragraphs, readTime, topList)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Text Input Box
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            placeholder = {
                Text(
                    "Paste or draft your article, markdown, blog post, or essay here to inspect live metadata...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentPurple,
                unfocusedBorderColor = BorderOutline,
                focusedContainerColor = SurfaceContainerLowest,
                unfocusedContainerColor = SurfaceContainerLowest
            ),
            shape = RoundedCornerShape(14.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
        )

        // Metrics Grid
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(modifier = Modifier.weight(1f), count = "$wordCount", label = "Words", color = AccentPurple)
                MetricCard(modifier = Modifier.weight(1f), count = "$charCount", label = "Characters", color = AccentCyan)
                MetricCard(modifier = Modifier.weight(1f), count = "$noSpaceCount", label = "No Spaces", color = AccentPink)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(modifier = Modifier.weight(1f), count = "$sentenceCount", label = "Sentences", color = TextPrimary)
                MetricCard(modifier = Modifier.weight(1f), count = "$paragraphCount", label = "Paragraphs", color = TextPrimary)
                MetricCard(modifier = Modifier.weight(1f), count = "${readingMin}m", label = "Reading Time", color = AccentPurple)
            }
        }

        // Keywords & Clear Draft
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.8f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("TOP KEYWORDS", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (topKeywords.isEmpty()) {
                        Text("Type longer words to calculate", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            topKeywords.forEach { (word, count) ->
                                Text(
                                    text = "$word ($count)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = JetBrainsMonoFamily,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = AccentPurple,
                                    modifier = Modifier
                                        .background(SurfaceContainerHigh, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                TextButton(
                    onClick = {
                        textInput = ""
                        onCopyText("Text draft cleared.")
                    }
                ) {
                    Text("Clear Draft", color = ErrorColor, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    count: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = JetBrainsMonoFamily,
                    fontWeight = FontWeight.Bold
                ),
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        }
    }
}

private data class AnalysisResult(
    val wordCount: Int,
    val charCount: Int,
    val noSpaceCount: Int,
    val sentenceCount: Int,
    val paragraphCount: Int,
    val readingMin: Int,
    val topKeywords: List<Pair<String, Int>>
)
