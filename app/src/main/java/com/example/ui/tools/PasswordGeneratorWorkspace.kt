package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.security.SecureRandom
import kotlin.math.log2

@Composable
fun PasswordGeneratorWorkspace(
    onCopyText: (String) -> Unit
) {
    var length by remember { mutableFloatStateOf(16f) }
    var incUpper by remember { mutableStateOf(true) }
    var incLower by remember { mutableStateOf(true) }
    var incNums by remember { mutableStateOf(true) }
    var incSyms by remember { mutableStateOf(true) }

    var refreshTrigger by remember { mutableIntStateOf(0) }

    val password = remember(length, incUpper, incLower, incNums, incSyms, refreshTrigger) {
        generatePassword(
            len = length.toInt(),
            upper = incUpper,
            lower = incLower,
            nums = incNums,
            syms = incSyms
        )
    }

    val (entropyBits, entropyRating, meterProgress) = remember(password, length, incUpper, incLower, incNums, incSyms) {
        var pool = 0
        if (incUpper) pool += 26
        if (incLower) pool += 26
        if (incNums) pool += 10
        if (incSyms) pool += 30
        if (pool == 0) pool = 26

        val bits = (length * log2(pool.toDouble())).toInt()
        when {
            bits >= 80 -> Triple(bits, "Military Grade", 1.0f)
            bits >= 60 -> Triple(bits, "Strong", 0.75f)
            bits >= 40 -> Triple(bits, "Moderate", 0.5f)
            else -> Triple(bits, "Weak", 0.25f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Output Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = password,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = JetBrainsMonoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = AccentPurple,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { refreshTrigger++ },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainerHighest)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Re-roll",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Button(
                        onClick = { onCopyText(password) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentPurple,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // Entropy meter
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cryptographic Entropy Rating:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    text = "$entropyRating (~$entropyBits bits)",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = AccentCyan
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHighest)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(meterProgress)
                        .fillMaxHeight()
                        .background(Brush.horizontalGradient(listOf(AccentPurple, AccentCyan)))
                )
            }
        }

        // Length Slider Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Length", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text(
                        "${length.toInt()} chars",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentPurple
                    )
                }

                Slider(
                    value = length,
                    onValueChange = { length = it },
                    valueRange = 8f..48f,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentPurple,
                        activeTrackColor = AccentPurple,
                        inactiveTrackColor = SurfaceContainerHighest
                    )
                )
            }
        }

        // Rule Checkboxes
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    RuleCheckbox(
                        modifier = Modifier.weight(1f),
                        checked = incUpper,
                        label = "Uppercase (A-Z)",
                        onCheckedChange = { incUpper = it }
                    )
                    RuleCheckbox(
                        modifier = Modifier.weight(1f),
                        checked = incLower,
                        label = "Lowercase (a-z)",
                        onCheckedChange = { incLower = it }
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    RuleCheckbox(
                        modifier = Modifier.weight(1f),
                        checked = incNums,
                        label = "Numbers (0-9)",
                        onCheckedChange = { incNums = it }
                    )
                    RuleCheckbox(
                        modifier = Modifier.weight(1f),
                        checked = incSyms,
                        label = "Symbols (!@#$)",
                        onCheckedChange = { incSyms = it }
                    )
                }
            }
        }
    }
}

@Composable
fun RuleCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    label: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = AccentPurple,
                uncheckedColor = TextMuted,
                checkmarkColor = Color.White
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary
        )
    }
}

private fun generatePassword(len: Int, upper: Boolean, lower: Boolean, nums: Boolean, syms: Boolean): String {
    val rng = SecureRandom()
    val upperChars = "ABCDEFGHJKLMNPQRSTUVWXYZ"
    val lowerChars = "abcdefghijkmnopqrstuvwxyz"
    val numChars = "23456789"
    val symChars = "!@#$%^&*()-_=+[]{}|;:,.<>?"

    var pool = ""
    if (upper) pool += upperChars
    if (lower) pool += lowerChars
    if (nums) pool += numChars
    if (syms) pool += symChars

    if (pool.isEmpty()) pool = lowerChars

    val sb = StringBuilder(len)
    for (i in 0 until len) {
        val idx = rng.nextInt(pool.length)
        sb.append(pool[idx])
    }
    return sb.toString()
}
