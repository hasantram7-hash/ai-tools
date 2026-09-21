package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun BmiHealthWorkspace(
    onCopyText: (String) -> Unit
) {
    var heightCmStr by remember { mutableStateOf("175") }
    var weightKgStr by remember { mutableStateOf("70") }
    var isMetric by remember { mutableStateOf(true) }

    val heightVal = heightCmStr.toDoubleOrNull() ?: 0.0
    val weightVal = weightKgStr.toDoubleOrNull() ?: 0.0

    val (bmi, category, color, advice, healthyRange) = remember(heightVal, weightVal, isMetric) {
        if (heightVal <= 0.0 || weightVal <= 0.0) {
            Tuple5(0.0, "Enter values", TextMuted, "Enter valid height and weight to calculate your BMI.", "18.5 - 24.9")
        } else {
            val hM = heightVal / 100.0
            val calculated = weightVal / (hM * hM)
            val rounded = (calculated * 10).roundToInt() / 10.0

            val minHealthyKg = (18.5 * hM * hM * 10).roundToInt() / 10.0
            val maxHealthyKg = (24.9 * hM * hM * 10).roundToInt() / 10.0
            val rangeStr = "$minHealthyKg kg - $maxHealthyKg kg"

            when {
                calculated < 18.5 -> Tuple5(
                    rounded,
                    "Underweight",
                    AccentCyan,
                    "Focus on nutrient-dense meals with balanced proteins and strength training to build healthy muscle mass.",
                    rangeStr
                )
                calculated < 25.0 -> Tuple5(
                    rounded,
                    "Healthy Weight",
                    Color(0xFF10B981),
                    "Great job! Your weight is in the optimal range for your height. Maintain a balanced diet and regular activity.",
                    rangeStr
                )
                calculated < 30.0 -> Tuple5(
                    rounded,
                    "Overweight",
                    Color(0xFFF59E0B),
                    "Consider adopting moderate daily cardiovascular exercise (30 mins walk) and mindful portion control.",
                    rangeStr
                )
                else -> Tuple5(
                    rounded,
                    "Obesity Range",
                    Color(0xFFEF4444),
                    "Consult a healthcare professional or nutritionist for personalized guidance on sustainable lifestyle changes.",
                    rangeStr
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Result Banner
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
            border = BorderStroke(1.dp, color.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Your BMI Score",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = if (bmi > 0) bmi.toString() else "--",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 38.sp
                        ),
                        color = color
                    )
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = color
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Inputs Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = BorderStroke(1.dp, BorderOutline.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = heightCmStr,
                        onValueChange = { heightCmStr = it.filter { ch -> ch.isDigit() || ch == '.' } },
                        label = { Text("Height (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = weightKgStr,
                        onValueChange = { weightKgStr = it.filter { ch -> ch.isDigit() || ch == '.' } },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Quick weight presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("60", "65", "70", "75", "80", "85").forEach { preset ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (weightKgStr == preset) AccentPurple else SurfaceContainerLow,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { weightKgStr = preset }
                        ) {
                            Text(
                                text = "${preset}kg",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (weightKgStr == preset) Color.White else TextPrimary,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Health Recommendations Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
            border = BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f))
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
                        text = "Healthy Ideal Weight Range",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = healthyRange,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                HorizontalDivider(color = BorderOutline.copy(alpha = 0.2f))

                Text(
                    text = "Recommendation:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = advice,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )

                Button(
                    onClick = {
                        val report = "BMI Health Report\nScore: $bmi ($category)\nIdeal Weight Range: $healthyRange\nAdvice: $advice"
                        onCopyText(report)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Health Report")
                }
            }
        }
    }
}

private data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
