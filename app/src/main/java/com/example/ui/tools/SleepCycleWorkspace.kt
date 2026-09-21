package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.util.*

@Composable
fun SleepCycleWorkspace(
    onCopyText: (String) -> Unit
) {
    // Mode: 0 = Wake up at specific time (when should I sleep?), 1 = Sleep now (when should I wake up?)
    var mode by remember { mutableIntStateOf(1) }
    var targetHour by remember { mutableIntStateOf(7) }
    var targetMinute by remember { mutableIntStateOf(0) }

    // Natural 90-minute REM sleep cycle calculation + 14 minutes avg to fall asleep
    val sleepOptions = remember(mode, targetHour, targetMinute) {
        val results = mutableListOf<SleepRecommendation>()
        val calendar = Calendar.getInstance()

        if (mode == 1) {
            // Sleeping right now
            val baseTime = calendar.apply { add(Calendar.MINUTE, 14) } // 14 mins to fall asleep
            for (cycles in 6 downTo 3) {
                val cycleCal = baseTime.clone() as Calendar
                cycleCal.add(Calendar.MINUTE, cycles * 90)
                val hour12 = if (cycleCal.get(Calendar.HOUR) == 0) 12 else cycleCal.get(Calendar.HOUR)
                val amPm = if (cycleCal.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                val minStr = String.format(Locale.US, "%02d", cycleCal.get(Calendar.MINUTE))
                val totalHours = cycles * 1.5
                val label = when (cycles) {
                    6 -> "9.0 hrs (Optimal, 6 cycles)"
                    5 -> "7.5 hrs (Suggested, 5 cycles)"
                    4 -> "6.0 hrs (Power sleep, 4 cycles)"
                    else -> "4.5 hrs (Bare minimum, 3 cycles)"
                }
                results.add(SleepRecommendation(timeStr = "$hour12:$minStr $amPm", duration = label, cycles = cycles))
            }
        } else {
            // Wake up at specific target
            val targetCal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, targetHour)
                set(Calendar.MINUTE, targetMinute)
            }
            for (cycles in 6 downTo 3) {
                val cycleCal = targetCal.clone() as Calendar
                cycleCal.add(Calendar.MINUTE, -(cycles * 90 + 14))
                val hour12 = if (cycleCal.get(Calendar.HOUR) == 0) 12 else cycleCal.get(Calendar.HOUR)
                val amPm = if (cycleCal.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                val minStr = String.format(Locale.US, "%02d", cycleCal.get(Calendar.MINUTE))
                val label = when (cycles) {
                    6 -> "9.0 hrs (Optimal, 6 cycles)"
                    5 -> "7.5 hrs (Suggested, 5 cycles)"
                    4 -> "6.0 hrs (Power sleep, 4 cycles)"
                    else -> "4.5 hrs (Bare minimum, 3 cycles)"
                }
                results.add(SleepRecommendation(timeStr = "$hour12:$minStr $amPm", duration = label, cycles = cycles))
            }
        }
        results
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Selector Tab
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceContainerHigh)
                .padding(4.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (mode == 1) AccentPurple else Color.Transparent,
                modifier = Modifier
                    .weight(1f)
                    .clickable { mode = 1 }
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = if (mode == 1) Color.White else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "I'm sleeping now",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (mode == 1) Color.White else TextSecondary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (mode == 0) AccentPurple else Color.Transparent,
                modifier = Modifier
                    .weight(1f)
                    .clickable { mode = 0 }
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = if (mode == 0) Color.White else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "I need to wake up at",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (mode == 0) Color.White else TextSecondary
                    )
                }
            }
        }

        if (mode == 0) {
            // Target wake time picker buttons
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Select Wake Up Time:",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(6 to "6:00 AM", 7 to "7:00 AM", 8 to "8:00 AM", 9 to "9:00 AM").forEach { (hr, txt) ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (targetHour == hr) AccentCyan else SurfaceContainerLow,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { targetHour = hr; targetMinute = 0 }
                            ) {
                                Text(
                                    text = txt,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (targetHour == hr) Color.White else TextPrimary,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Cycle recommendations list
        Text(
            text = if (mode == 1) "Suggested Times to Wake Up Clean & Refreshed:" else "Times You Should Fall Asleep Tonight:",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )

        sleepOptions.forEachIndexed { index, rec ->
            val isBest = index == 1 // 5 cycles (7.5 hrs) is ideal
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isBest) AccentPurple.copy(alpha = 0.1f) else SurfaceContainerLowest
                ),
                border = BorderStroke(
                    1.dp,
                    if (isBest) AccentPurple.copy(alpha = 0.5f) else BorderOutline.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCopyText("Recommended Alarm: ${rec.timeStr} (${rec.duration})")
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isBest) AccentPurple else SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${rec.cycles}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isBest) Color.White else TextPrimary
                            )
                        }

                        Column {
                            Text(
                                text = rec.timeStr,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = rec.duration,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isBest) AccentPurple else TextSecondary
                            )
                        }
                    }

                    if (isBest) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentPurple
                        ) {
                            Text(
                                text = "Most Natural",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

private data class SleepRecommendation(val timeStr: String, val duration: String, val cycles: Int)
