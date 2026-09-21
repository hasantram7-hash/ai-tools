package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.util.*

@Composable
fun AgeCalculatorWorkspace(
    onCopyText: (String) -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    val currentYear = calendar.get(Calendar.YEAR)

    var birthYear by remember { mutableIntStateOf(2000) }
    var birthMonth by remember { mutableIntStateOf(1) } // 1-12
    var birthDay by remember { mutableIntStateOf(1) }

    // Computed states
    val (years, months, days, daysUntilBday, totalHours) = remember(birthYear, birthMonth, birthDay) {
        val birthCal = Calendar.getInstance().apply {
            set(birthYear, birthMonth - 1, birthDay, 0, 0, 0)
        }
        val nowCal = Calendar.getInstance()

        var y = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
        var m = nowCal.get(Calendar.MONTH) - birthCal.get(Calendar.MONTH)
        var d = nowCal.get(Calendar.DAY_OF_MONTH) - birthCal.get(Calendar.DAY_OF_MONTH)

        if (d < 0) {
            m -= 1
            val prevMonthCal = Calendar.getInstance().apply {
                set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), 1)
                add(Calendar.DAY_OF_MONTH, -1)
            }
            d += prevMonthCal.get(Calendar.DAY_OF_MONTH)
        }
        if (m < 0) {
            y -= 1
            m += 12
        }
        val safeY = maxOf(0, y)
        val safeM = maxOf(0, m)
        val safeD = maxOf(0, d)

        val nextBday = Calendar.getInstance().apply {
            set(Calendar.MONTH, birthMonth - 1)
            set(Calendar.DAY_OF_MONTH, birthDay)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        if (nextBday.before(nowCal)) {
            nextBday.add(Calendar.YEAR, 1)
        }
        val diffMs = nextBday.timeInMillis - nowCal.timeInMillis
        val daysUntil = maxOf(0, (diffMs / (1000 * 60 * 60 * 24)).toInt())

        val totalElapsedMs = nowCal.timeInMillis - birthCal.timeInMillis
        val hours = maxOf(0L, totalElapsedMs / (1000 * 60 * 60))

        AgeCalcResult(safeY, safeM, safeD, daysUntil, hours)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Birthdate Picker Controls
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Date of Birth",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Month selector
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Month", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        NumberStepper(
                            value = birthMonth,
                            min = 1,
                            max = 12,
                            label = "$birthMonth",
                            onValueChange = { birthMonth = it }
                        )
                    }
                    // Day selector
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Day", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        NumberStepper(
                            value = birthDay,
                            min = 1,
                            max = 31,
                            label = "$birthDay",
                            onValueChange = { birthDay = it }
                        )
                    }
                    // Year selector
                    Column(modifier = Modifier.weight(1.3f)) {
                        Text("Year", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        NumberStepper(
                            value = birthYear,
                            min = 1920,
                            max = currentYear,
                            label = "$birthYear",
                            onValueChange = { birthYear = it }
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Calculates leap years automatically & live milestone",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // Output Metric Tiles: Years, Months, Days
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricTile(
                modifier = Modifier.weight(1f),
                value = "$years",
                label = "Years",
                color = AccentPurple
            )
            MetricTile(
                modifier = Modifier.weight(1f),
                value = "$months",
                label = "Months",
                color = AccentCyan
            )
            MetricTile(
                modifier = Modifier.weight(1f),
                value = "$days",
                label = "Days",
                color = AccentPink
            )
        }

        // Birthday Countdown Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.8f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Days until next birthday:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "$daysUntilBday days away",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentCyan
                    )
                }

                val progress = ((365 - daysUntilBday).toFloat() / 365f).coerceIn(0.05f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHighest)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(listOf(AccentPurple, AccentCyan))
                            )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total approximate hours lived:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Text(
                        text = "%,d hrs".format(totalHours),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextPrimary
                    )
                }
            }
        }

        // Copy Button
        Button(
            onClick = {
                val summary = "Age Milestone: $years years, $months months, $days days lived. Total $totalHours hours. Computed on Collection of Ram."
                onCopyText(summary)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SurfaceContainerHighest,
                contentColor = TextPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Copy Lifespan Summary", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun NumberStepper(
    value: Int,
    min: Int,
    max: Int,
    label: String,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainerLowest)
            .border(1.dp, BorderOutline.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { if (value > min) onValueChange(value - 1) },
            modifier = Modifier.size(28.dp)
        ) {
            Text("-", color = AccentPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = { if (value < max) onValueChange(value + 1) },
            modifier = Modifier.size(28.dp)
        ) {
            Text("+", color = AccentCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun MetricTile(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.9f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

private data class AgeCalcResult(
    val years: Int,
    val months: Int,
    val days: Int,
    val daysUntilBday: Int,
    val totalHours: Long
)
