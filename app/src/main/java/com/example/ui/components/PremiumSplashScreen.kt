package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PremiumSplashScreen(
    onFinished: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0.05f) }
    var currentStep by remember { mutableStateOf("Initializing Titanium Engine...") }

    // Infinite rotation for the luxury orbit ring
    val infiniteTransition = rememberInfiniteTransition(label = "orbit_transition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_rotation"
    )

    val counterRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "counter_rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emblem_pulse"
    )

    // Dynamic loading sequence over 2 seconds
    LaunchedEffect(Unit) {
        val steps = listOf(
            0.15f to "Calibrating Titanium Gray Palette...",
            0.38f to "Loading 22+ Creator & Everyday Tools...",
            0.65f to "Configuring Gemini AI Assistant...",
            0.88f to "Optimizing Client-Side Memory...",
            1.00f to "Welcome to Collection of Ram"
        )

        for ((targetProg, text) in steps) {
            currentStep = text
            val startProg = progress
            val subSteps = 15
            for (i in 1..subSteps) {
                progress = startProg + (targetProg - startProg) * (i.toFloat() / subSteps)
                delay(22)
            }
            delay(120)
        }
        delay(320)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .testTag("premium_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Ambient luxury glow spheres
        Box(
            modifier = Modifier
                .size(340.dp)
                .offset(y = (-60).dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentPink.copy(alpha = 0.16f),
                            AccentRose.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentWhite.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // Animated Luxury Dual-Ring Orbit
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                // Outer rotating gradient ring
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotation)
                ) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                AccentPink,
                                AccentWhite,
                                AccentRose,
                                Color.Transparent,
                                AccentPink
                            )
                        ),
                        startAngle = 0f,
                        sweepAngle = 280f,
                        useCenter = false,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Inner counter-rotating ring
                Canvas(
                    modifier = Modifier
                        .size(116.dp)
                        .rotate(counterRotation)
                ) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                AccentWhite,
                                AccentPink.copy(alpha = 0.4f),
                                Color.Transparent,
                                AccentWhite
                            )
                        ),
                        startAngle = 45f,
                        sweepAngle = 240f,
                        useCenter = false,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Center Monogram Badge
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    SurfaceContainerLow,
                                    SurfaceContainerLowest
                                )
                            )
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.linearGradient(
                                listOf(
                                    AccentWhite,
                                    AccentPink,
                                    AccentRose
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AccentRose,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "CR",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.5.sp,
                                fontSize = 26.sp
                            ),
                            color = AccentWhite
                        )
                    }
                }
            }

            // Brand Typography
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "COLLECTION OF RAM",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.5.sp,
                        fontSize = 20.sp
                    ),
                    color = AccentWhite,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "By Hem Narayan (Ram)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    ),
                    color = AccentRose,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerHigh.copy(alpha = 0.7f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.5f)),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "22+ Everyday & Creator Engines",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dynamic Progress Indicator
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Sleek progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(SurfaceContainerHigh)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        AccentPink,
                                        AccentRose,
                                        AccentWhite
                                    )
                                )
                            )
                    )
                }

                // Progress Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentStep,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        maxLines = 1
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AccentRose
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Skip / Instant Access Button
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceContainerLow.copy(alpha = 0.8f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.4f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onFinished() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Enter Collection",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AccentWhite
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = AccentRose,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
