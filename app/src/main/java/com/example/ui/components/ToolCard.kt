package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ToolItem
import com.example.ui.theme.*

@Composable
fun ToolCard(
    tool: ToolItem,
    onLaunch: (ToolItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        tool.primaryColor.copy(alpha = 0.35f),
                        BorderOutline.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onLaunch(tool) }
            .testTag("tool_card_${tool.id}"),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerLow.copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Top row: Icon Box and Category Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(tool.primaryColor, tool.secondaryColor)
                                )
                            )
                            .padding(1.5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceContainerLowest),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getToolIcon(tool.iconName),
                                contentDescription = tool.title,
                                tint = tool.primaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(tool.primaryColor.copy(alpha = 0.15f))
                            .border(1.dp, tool.primaryColor.copy(alpha = 0.3f), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = tool.categoryLabel,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = tool.primaryColor
                        )
                    }
                }

                // Title and description
                Column {
                    Text(
                        text = tool.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = SoraFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tool.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Micro Metric Quick Peek Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLowest.copy(alpha = 0.7f))
                        .border(1.dp, BorderOutline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "${tool.metricLabel}:",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = tool.metricValue,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = JetBrainsMonoFamily,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = tool.statusBadge,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = tool.secondaryColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer row: Tag & Launch button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.dp,
                        color = Color.Transparent
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tool.tagText,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )

                Button(
                    onClick = { onLaunch(tool) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPink.copy(alpha = 0.15f),
                        contentColor = AccentWhite
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentPink.copy(alpha = 0.4f)),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("launch_btn_${tool.id}")
                ) {
                    Text(
                        text = "Launch",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
                        color = AccentWhite
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = AccentPink
                    )
                }
            }
        }
    }
}

fun getToolIcon(name: String): ImageVector {
    return when (name) {
        "calendar_clock" -> Icons.Default.CalendarMonth
        "sync_alt" -> Icons.Default.SyncAlt
        "qr_code_scanner" -> Icons.Default.QrCode
        "photo_size_select_small" -> Icons.Default.Compress
        "key" -> Icons.Default.VpnKey
        "receipt_long" -> Icons.Default.ReceiptLong
        "palette" -> Icons.Default.Palette
        "format_align_left" -> Icons.Default.FormatAlignLeft
        "favorite" -> Icons.Default.Favorite
        "auto_fix_high" -> Icons.Default.AutoFixHigh
        "auto_awesome" -> Icons.Default.AutoAwesome
        "smart_display" -> Icons.Default.SmartDisplay
        "chat_bubble" -> Icons.Default.ChatBubble
        "text_fields" -> Icons.Default.TextFields
        "casino" -> Icons.Default.Casino
        "play_arrow" -> Icons.Default.PlayArrow
        "wallpaper" -> Icons.Default.Wallpaper
        "self_improvement" -> Icons.Default.SelfImprovement
        else -> Icons.Default.Widgets
    }
}
