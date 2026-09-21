package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import kotlinx.coroutines.delay

@Composable
fun PomodoroTimerWorkspace(
    onCopyText: (String) -> Unit
) {
    // Modes: 25 min work, 5 min short break, 15 min long break
    var mode by remember { mutableIntStateOf(25) } // minutes
    var secondsRemaining by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var completedSessions by remember { mutableIntStateOf(0) }

    val totalSeconds = mode * 60
    val progress = if (totalSeconds > 0) (secondsRemaining.toFloat() / totalSeconds).coerceIn(0f, 1f) else 0f

    val minutesStr = String.format("%02d", secondsRemaining / 60)
    val secondsStr = String.format("%02d", secondsRemaining % 60)

    LaunchedEffect(isRunning, secondsRemaining) {
        if (isRunning && secondsRemaining > 0) {
            delay(1000)
            secondsRemaining -= 1
        } else if (isRunning && secondsRemaining == 0) {
            isRunning = false
            if (mode == 25) {
                completedSessions += 1
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Selector Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                25 to "Work (25m)",
                5 to "Short Break (5m)",
                15 to "Long Break (15m)"
            ).forEach { (m, label) ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (mode == m) AccentPurple else SurfaceContainerLow,
                    border = BorderStroke(1.dp, if (mode == m) AccentPurple else BorderOutline.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            mode = m
                            secondsRemaining = m * 60
                            isRunning = false
                        }
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (mode == m) Color.White else TextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Circular Timer Display
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = BorderStroke(1.dp, BorderOutline.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(170.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f - progress },
                        modifier = Modifier.fillMaxSize(),
                        color = if (mode == 25) AccentPurple else AccentCyan,
                        strokeWidth = 10.dp,
                        trackColor = SurfaceContainerHigh
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$minutesStr:$secondsStr",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 42.sp
                            ),
                            color = TextPrimary
                        )
                        Text(
                            text = if (isRunning) "Deep Focus Active" else if (secondsRemaining == 0) "Session Finished!" else "Paused",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isRunning) (if (mode == 25) AccentPurple else AccentCyan) else TextSecondary
                        )
                    }
                }

                // Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            secondsRemaining = mode * 60
                            isRunning = false
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset", tint = TextPrimary)
                    }

                    Button(
                        onClick = { isRunning = !isRunning },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mode == 25) AccentPurple else AccentCyan
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRunning) "Pause" else "Start Timer",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Sessions completed badge
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
            border = BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Completed Focus Blocks Today: $completedSessions",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )

                TextButton(onClick = { onCopyText("Completed $completedSessions Pomodoro focus blocks today with Collection of Ram!") }) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share")
                }
            }
        }
    }
}
