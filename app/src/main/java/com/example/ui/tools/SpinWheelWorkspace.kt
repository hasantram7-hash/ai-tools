package com.example.ui.tools

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SpinWheelWorkspace(
    onCopyText: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var optionsList by remember {
        mutableStateOf(
            listOf("Pizza 🍕", "Burger 🍔", "Biryani 🍛", "Salad 🥗", "Tacos 🌮", "Sushi 🍱")
        )
    }
    var newOptionText by remember { mutableStateOf("") }
    var selectedWinner by remember { mutableStateOf<String?>(null) }
    var isSpinning by remember { mutableStateOf(false) }

    val rotationAngle = remember { Animatable(0f) }

    val sliceColors = remember {
        listOf(
            Color(0xFF8B5CF6), // Purple
            Color(0xFF06B6D4), // Cyan
            Color(0xFFF43F5E), // Rose
            Color(0xFFF59E0B), // Amber
            Color(0xFF10B981), // Emerald
            Color(0xFF6366F1), // Indigo
            Color(0xFFEC4899), // Pink
            Color(0xFF14B8A6)  // Teal
        )
    }

    fun spinTheWheel() {
        if (optionsList.size < 2 || isSpinning) return
        isSpinning = true
        selectedWinner = null

        coroutineScope.launch {
            val randomTurns = 5 + Random.nextInt(4)
            val randomExtraDegree = Random.nextFloat() * 360f
            val targetDegree = rotationAngle.value + (randomTurns * 360f) + randomExtraDegree

            rotationAngle.animateTo(
                targetValue = targetDegree,
                animationSpec = tween(
                    durationMillis = 3500,
                    easing = androidx.compose.animation.core.FastOutSlowInEasing
                )
            )

            // Calculate which slice stopped at the top indicator (270 degrees or 90 offset)
            val normalized = (rotationAngle.value % 360f + 360f) % 360f
            val sliceSize = 360f / optionsList.size
            // Pointer at 270 deg (top)
            val pointerDeg = (270f - normalized + 360f) % 360f
            val winningIndex = (pointerDeg / sliceSize).toInt() % optionsList.size

            val winner = optionsList[winningIndex]
            selectedWinner = winner
            isSpinning = false
            onCopyText("Spin Result: $winner!")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Wheel Canvas with Pointer
        Box(
            modifier = Modifier
                .size(260.dp)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension / 2f
                val sliceAngle = 360f / optionsList.size

                val currentRot = rotationAngle.value

                optionsList.forEachIndexed { index, text ->
                    val startAngle = currentRot + (index * sliceAngle)
                    val col = sliceColors[index % sliceColors.size]

                    // Draw arc slice
                    drawArc(
                        color = col,
                        startAngle = startAngle,
                        sweepAngle = sliceAngle,
                        useCenter = true,
                        size = Size(radius * 2, radius * 2),
                        topLeft = Offset(center.x - radius, center.y - radius)
                    )

                    // Draw slice text label
                    val midAngleRad = (startAngle + sliceAngle / 2f) * (PI / 180f)
                    val labelRadius = radius * 0.65f
                    val textX = (center.x + labelRadius * cos(midAngleRad)).toFloat()
                    val textY = (center.y + labelRadius * sin(midAngleRad)).toFloat()

                    drawIntoCanvas { canvas ->
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 28f
                            textAlign = android.graphics.Paint.Align.CENTER
                            isFakeBoldText = true
                            setShadowLayer(4f, 0f, 2f, android.graphics.Color.BLACK)
                        }
                        canvas.nativeCanvas.drawText(
                            text.take(8),
                            textX,
                            textY + 10f,
                            paint
                        )
                    }
                }

                // Center hub
                drawCircle(
                    color = Color(0xFF0F172A),
                    radius = 28f,
                    center = center
                )
                drawCircle(
                    color = Color.White,
                    radius = 12f,
                    center = center
                )
            }

            // Top Triangle Indicator / Pointer
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-6).dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    color = Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(width = 20.dp, height = 24.dp)
                ) {}
            }
        }

        // Winner announcement
        selectedWinner?.let { winner ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AccentCyan)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "🎉 WINNER: ", style = MaterialTheme.typography.titleMedium, color = AccentCyan)
                    Text(
                        text = winner,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }

        // Spin Action Button
        Button(
            onClick = { spinTheWheel() },
            enabled = !isSpinning && optionsList.size >= 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("spin_wheel_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentPurple,
                contentColor = Color.White
            )
        ) {
            Icon(imageVector = Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isSpinning) "Spinning..." else "Spin The Wheel!", style = MaterialTheme.typography.titleMedium)
        }

        // Options Manager
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Choices & Options (${optionsList.size})",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newOptionText,
                        onValueChange = { newOptionText = it },
                        placeholder = { Text("Add choice (e.g. Movie, Goa, Gym...)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Button(
                        onClick = {
                            if (newOptionText.isNotBlank()) {
                                optionsList = optionsList + newOptionText.trim()
                                newOptionText = ""
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentCyan, contentColor = Color(0xFF003840))
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }

                // Chips of items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    optionsList.take(6).forEach { opt ->
                        InputChip(
                            selected = false,
                            onClick = {
                                if (optionsList.size > 2) {
                                    optionsList = optionsList - opt
                                }
                            },
                            label = { Text(opt, style = MaterialTheme.typography.bodySmall) },
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(14.dp))
                            }
                        )
                    }
                }
            }
        }
    }
}
