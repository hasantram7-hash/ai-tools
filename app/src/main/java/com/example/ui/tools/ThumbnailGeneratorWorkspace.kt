package com.example.ui.tools

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class ThumbnailStyle(val label: String, val bgGradStart: Int, val bgGradEnd: Int, val tagColor: Int) {
    NEON_TECH("Cyber Neon", 0xFF0B1326.toInt(), 0xFF1E1B4B.toInt(), 0xFF22D3EE.toInt()),
    FIRE_VIRAL("High Voltage", 0xFF18080C.toInt(), 0xFF450A0A.toInt(), 0xFFFB7185.toInt()),
    CLEAN_DEV("Cosmic Studio", 0xFF030712.toInt(), 0xFF312E81.toInt(), 0xFFA78BFA.toInt()),
    MINIMAL_DARK("Obsidian Gold", 0xFF0A0A0A.toInt(), 0xFF1C1917.toInt(), 0xFFFBBF24.toInt())
}

@Composable
fun ThumbnailGeneratorWorkspace(
    onCopyText: (String) -> Unit
) {
    var mainHeadline by remember { mutableStateOf("10X FASTER WITH THIS") }
    var subHeadline by remember { mutableStateOf("THE ULTIMATE WORKFLOW REVEALED") }
    var badgeTag by remember { mutableStateOf("SECRET METHOD") }
    var selectedStyle by remember { mutableStateOf(ThumbnailStyle.NEON_TECH) }

    // Fast dynamic 16:9 canvas thumbnail builder
    val thumbnailBitmap = remember(mainHeadline, subHeadline, badgeTag, selectedStyle) {
        renderThumbnailBitmap(
            headline = mainHeadline.ifEmpty { "10X FASTER WITH THIS" },
            subtitle = subHeadline.ifEmpty { "THE SECRETS REVEALED" },
            badge = badgeTag.ifEmpty { "PRO TIP" },
            style = selectedStyle
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Thumbnail 16:9 Live Preview Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderOutline.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 16:9 aspect ratio box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(selectedStyle.bgGradStart))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = thumbnailBitmap.asImageBitmap(),
                        contentDescription = "Thumbnail Preview",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AccentCyan)
                        )
                        Text(
                            text = "1280 × 720 HD YouTube Aspect (16:9)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = {
                            val exportDetail = "YouTube Thumbnail Card: [Badge: $badgeTag] \"$mainHeadline\" | Sub: \"$subHeadline\" (Theme: ${selectedStyle.label}). Built with Collection of Ram."
                            onCopyText(exportDetail)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHighest,
                            contentColor = TextPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy Spec", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Style Selector
        Text("Select Visual Style Theme", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThumbnailStyle.entries.forEach { style ->
                val isSel = selectedStyle == style
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedStyle = style },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSel) SurfaceContainerHighest else SurfaceContainerHigh.copy(alpha = 0.6f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isSel) Color(style.tagColor) else BorderOutline.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color(style.tagColor))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = style.label.split(" ").first(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSel) TextPrimary else TextMuted
                        )
                    }
                }
            }
        }

        // Text input editors
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.5f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = badgeTag,
                    onValueChange = { badgeTag = it },
                    label = { Text("Badge Label") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("thumb_badge_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = BorderOutline,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = mainHeadline,
                    onValueChange = { mainHeadline = it },
                    label = { Text("Main High-Contrast Headline (BIG)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("thumb_headline_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = BorderOutline,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = subHeadline,
                    onValueChange = { subHeadline = it },
                    label = { Text("Secondary Accent Subtitle") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("thumb_subhead_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPink,
                        unfocusedBorderColor = BorderOutline,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }
    }
}

private fun renderThumbnailBitmap(headline: String, subtitle: String, badge: String, style: ThumbnailStyle): Bitmap {
    val width = 1280
    val height = 720
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Background gradient
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.shader = android.graphics.LinearGradient(
        0f, 0f, width.toFloat(), height.toFloat(),
        style.bgGradStart, style.bgGradEnd,
        android.graphics.Shader.TileMode.CLAMP
    )
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    paint.shader = null

    // Ambient tech glow circle in bottom right
    paint.color = style.tagColor
    paint.alpha = 40
    canvas.drawCircle(width * 0.85f, height * 0.7f, 280f, paint)

    // Grid lines / decorative frame accent
    paint.color = android.graphics.Color.WHITE
    paint.alpha = 25
    paint.strokeWidth = 3f
    paint.style = Paint.Style.STROKE
    canvas.drawRoundRect(RectF(30f, 30f, width - 30f, height - 30f), 20f, 20f, paint)
    paint.style = Paint.Style.FILL

    // Pill Badge
    val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = style.tagColor
    }
    val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.BLACK
        textSize = 34f
        typeface = android.graphics.Typeface.DEFAULT_BOLD
    }
    val badgeWidth = badgeTextPaint.measureText(badge.uppercase()) + 60f
    val badgeRect = RectF(80f, 90f, 80f + badgeWidth, 160f)
    canvas.drawRoundRect(badgeRect, 20f, 20f, badgePaint)
    canvas.drawText(badge.uppercase(), 110f, 138f, badgeTextPaint)

    // Main Big Headline (High Clickthrough Rate font style)
    val headPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = 76f
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        setShadowLayer(16f, 0f, 6f, android.graphics.Color.BLACK)
    }

    // Split headline if too wide
    if (headline.length > 20) {
        val mid = headline.indexOf(" ", headline.length / 2)
        if (mid != -1) {
            val l1 = headline.substring(0, mid).uppercase()
            val l2 = headline.substring(mid + 1).uppercase()
            canvas.drawText(l1, 80f, 290f, headPaint)
            headPaint.color = style.tagColor
            canvas.drawText(l2, 80f, 390f, headPaint)
        } else {
            canvas.drawText(headline.uppercase(), 80f, 310f, headPaint)
        }
    } else {
        canvas.drawText(headline.uppercase(), 80f, 310f, headPaint)
    }

    // Subtitle accent text
    val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.parseColor("#E2E8F0")
        textSize = 38f
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        letterSpacing = 0.05f
    }
    canvas.drawText(subtitle.uppercase(), 80f, 490f, subPaint)

    // Watermark / Brand Badge bottom right
    val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        alpha = 140
        textSize = 28f
        typeface = android.graphics.Typeface.MONOSPACE
    }
    canvas.drawText("COLLECTION OF RAM • 4K ULTRA HD", 80f, 640f, brandPaint)

    return bitmap
}
