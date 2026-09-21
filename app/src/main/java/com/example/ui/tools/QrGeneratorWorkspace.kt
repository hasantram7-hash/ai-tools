package com.example.ui.tools

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun QrGeneratorWorkspace(
    onCopyText: (String) -> Unit
) {
    var payloadText by remember { mutableStateOf("https://collectionofram.tools/free-suite") }
    val colorOptions = listOf(
        AccentPurple to "Purple",
        AccentCyan to "Cyan",
        AccentPink to "Pink",
        Color(0xFF10B981) to "Emerald",
        Color(0xFFFFFFFF) to "White"
    )
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    val activeColor = colorOptions[selectedColorIndex].first

    var resolutionIndex by remember { mutableIntStateOf(1) } // 200, 300, 500
    val resolutions = listOf("200 x 200", "300 x 300 (Default)", "500 x 500 (HD)")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input text area
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Payload URL or String",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                OutlinedTextField(
                    value = payloadText,
                    onValueChange = { payloadText = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = BorderOutline,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                )

                // Foreground accent colors
                Text(
                    text = "Foreground Accent",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colorOptions.forEachIndexed { index, (color, _) ->
                        val isSelected = selectedColorIndex == index
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color.White else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorIndex = index }
                        )
                    }
                }
            }
        }

        // QR Canvas Matrix Visualizer
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.9f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0B1326))
                        .border(1.dp, BorderOutline.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    QrCanvas(
                        text = payloadText,
                        color = activeColor,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onCopyText("QR Matrix Data ($payloadText) ready for export.")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentPurple,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export PNG", style = MaterialTheme.typography.titleMedium)
                    }

                    OutlinedButton(
                        onClick = {
                            onCopyText(payloadText)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy String", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun QrCanvas(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val grid = 21
        val cellSize = size.width / grid

        // Draw dark background
        drawRect(color = Color(0xFF0B1326), size = size)

        var hash = 0
        for (ch in text) {
            hash = ((hash shl 5) - hash) + ch.code
        }

        // Draw Finder Patterns at (1,1), (1, 13), (13, 1)
        fun drawFinder(startR: Int, startC: Int) {
            for (i in 0 until 7) {
                for (j in 0 until 7) {
                    val isBorder = (i == 0 || i == 6 || j == 0 || j == 6)
                    val isCenter = (i in 2..4 && j in 2..4)
                    if (isBorder || isCenter) {
                        drawRect(
                            color = color,
                            topLeft = Offset((startC + j) * cellSize, (startR + i) * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }
            }
        }

        drawFinder(1, 1)
        drawFinder(1, grid - 8)
        drawFinder(grid - 8, 1)

        // Draw timing patterns
        for (i in 8 until grid - 8) {
            if (i % 2 == 0) {
                drawRect(
                    color = color,
                    topLeft = Offset(i * cellSize, 6 * cellSize),
                    size = Size(cellSize, cellSize)
                )
                drawRect(
                    color = color,
                    topLeft = Offset(6 * cellSize, i * cellSize),
                    size = Size(cellSize, cellSize)
                )
            }
        }

        // Data modules
        for (r in 0 until grid) {
            for (c in 0 until grid) {
                // Skip finder patterns
                if ((r < 9 && c < 9) || (r < 9 && c > grid - 10) || (r > grid - 10 && c < 9)) continue
                if (r == 6 || c == 6) continue

                val sample = sin((hash + r * 31 + c * 17).toDouble())
                if (sample > -0.05) {
                    drawRect(
                        color = color,
                        topLeft = Offset(c * cellSize + 0.5f, r * cellSize + 0.5f),
                        size = Size(cellSize - 1f, cellSize - 1f)
                    )
                }
            }
        }
    }
}
