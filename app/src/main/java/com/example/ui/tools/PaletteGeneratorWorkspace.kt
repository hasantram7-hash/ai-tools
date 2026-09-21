package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.random.Random

data class PaletteColor(
    val hex: String,
    val color: Color,
    val isLocked: Boolean = false
)

@Composable
fun PaletteGeneratorWorkspace(
    onCopyText: (String) -> Unit
) {
    var palette by remember {
        mutableStateOf(
            listOf(
                PaletteColor("#8B5CF6", AccentPurple, false),
                PaletteColor("#22D3EE", AccentCyan, false),
                PaletteColor("#F0ABFC", AccentPink, false),
                PaletteColor("#222A3D", Color(0xFF222A3D), false),
                PaletteColor("#0B1326", Color(0xFF0B1326), false)
            )
        )
    }

    fun randomizeUnlocked() {
        palette = palette.map { item ->
            if (item.isLocked) item
            else {
                val r = Random.nextInt(256)
                val g = Random.nextInt(256)
                val b = Random.nextInt(256)
                val hex = "#%02X%02X%02X".format(r, g, b)
                PaletteColor(hex, Color(r, g, b), false)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Swatches row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            palette.forEachIndexed { index, item ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = item.color),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Lock / Unlock button
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.45f))
                                .clickable {
                                    palette = palette.toMutableList().also { list ->
                                        list[index] = item.copy(isLocked = !item.isLocked)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (item.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = if (item.isLocked) "Unlock" else "Lock",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        // Hex pill + Copy
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.55f))
                                .clickable { onCopyText(item.hex) }
                                .padding(horizontal = 4.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.hex,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = JetBrainsMonoFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Tip: Lock swatches you want to preserve before generating new colors.",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { randomizeUnlocked() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentPurple,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Generate New", style = MaterialTheme.typography.titleMedium)
            }

            OutlinedButton(
                onClick = {
                    val css = ":root {\n" + palette.mapIndexed { idx, p ->
                        "  --color-ram-${idx + 1}: ${p.hex};"
                    }.joinToString("\n") + "\n}"
                    onCopyText(css)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export CSS", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
