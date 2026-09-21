package com.example.ui.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

enum class BgReplaceMode(val label: String) {
    TRANSPARENT("Transparent (PNG)"),
    SOLID_WHITE("Studio White"),
    NEON_GRADIENT("Neon Violet"),
    DARK_MODE("Obsidian Black"),
    CYAN_VIBE("Vibrant Cyan")
}

@Composable
fun PhotoBgRemoverWorkspace(
    onCopyText: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var originalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var processedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    var tolerance by remember { mutableFloatStateOf(35f) }
    var selectedMode by remember { mutableStateOf(BgReplaceMode.TRANSPARENT) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            scope.launch(Dispatchers.IO) {
                try {
                    val stream = context.contentResolver.openInputStream(uri)
                    val raw = BitmapFactory.decodeStream(stream)
                    stream?.close()
                    // Scale down for ultra fast mobile edge processing
                    val maxDim = 800
                    val scaled = if (raw.width > maxDim || raw.height > maxDim) {
                        val ratio = raw.width.toFloat() / raw.height.toFloat()
                        val w = if (ratio > 1f) maxDim else (maxDim * ratio).toInt()
                        val h = if (ratio > 1f) (maxDim / ratio).toInt() else maxDim
                        Bitmap.createScaledBitmap(raw, w, h, true)
                    } else raw

                    withContext(Dispatchers.Main) {
                        originalBitmap = scaled
                        processedBitmap = null
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        onCopyText("Could not load image: ${e.message}")
                    }
                }
            }
        }
    }

    fun removeBackgroundNow() {
        val src = originalBitmap ?: return
        isProcessing = true
        scope.launch(Dispatchers.Default) {
            val result = processEdgeBgRemoval(src, tolerance, selectedMode)
            withContext(Dispatchers.Main) {
                processedBitmap = result
                isProcessing = false
                onCopyText("Background removed successfully!")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Picker & Preview Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (processedBitmap != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when (selectedMode) {
                                    BgReplaceMode.TRANSPARENT -> SurfaceContainerLowest
                                    BgReplaceMode.SOLID_WHITE -> Color.White
                                    BgReplaceMode.NEON_GRADIENT -> AccentPurple
                                    BgReplaceMode.DARK_MODE -> Color(0xFF0F172A)
                                    BgReplaceMode.CYAN_VIBE -> AccentCyan
                                }
                            )
                            .border(1.dp, AccentCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = processedBitmap!!.asImageBitmap(),
                            contentDescription = "Cutout Preview",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = "Clean Subject Isolation Active",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AccentCyan
                    )
                } else if (originalBitmap != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLowest)
                            .border(1.dp, BorderOutline.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = originalBitmap!!.asImageBitmap(),
                            contentDescription = "Source Photo",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    // Empty state upload prompt
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLowest)
                            .clickable { photoPickerLauncher.launch("image/*") }
                            .border(1.5.dp, BorderOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = AccentCyan,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "Tap to pick a photo from gallery",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            Text(
                                text = "Supports portraits, products, stickers & logos",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { photoPickerLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHighest,
                            contentColor = TextPrimary
                        )
                    ) {
                        Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (originalBitmap == null) "Select Photo" else "Change Photo")
                    }

                    if (originalBitmap != null) {
                        Button(
                            onClick = { removeBackgroundNow() },
                            modifier = Modifier.weight(1.3f),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isProcessing,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentCyan,
                                contentColor = Color(0xFF003840)
                            )
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color(0xFF003840), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Analyzing Edges...")
                            } else {
                                Icon(imageVector = Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Remove Background", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Tolerance & Background Color Presets
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Edge Color Threshold", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                    Text(
                        "${tolerance.toInt()}%",
                        style = MaterialTheme.typography.labelMedium.copy(fontFamily = JetBrainsMonoFamily, fontWeight = FontWeight.Bold),
                        color = AccentCyan
                    )
                }

                Slider(
                    value = tolerance,
                    onValueChange = { tolerance = it },
                    valueRange = 10f..80f,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentCyan,
                        activeTrackColor = AccentCyan,
                        inactiveTrackColor = SurfaceContainerHighest
                    )
                )

                Text("Background Replacement Mode", style = MaterialTheme.typography.labelSmall, color = TextMuted)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BgReplaceMode.entries.forEach { mode ->
                        val isSel = selectedMode == mode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) AccentCyan else SurfaceContainerHighest)
                                .clickable {
                                    selectedMode = mode
                                    if (originalBitmap != null) removeBackgroundNow()
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mode.name.take(4),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSel) Color(0xFF003840) else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Privacy indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "Client-side edge chrominance isolation • Zero images leave phone",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}

private fun processEdgeBgRemoval(src: Bitmap, toleranceVal: Float, mode: BgReplaceMode): Bitmap {
    val width = src.width
    val height = src.height
    val outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val pixels = IntArray(width * height)
    src.getPixels(pixels, 0, width, 0, 0, width, height)

    // Sample background colors from four corners
    val c1 = pixels[0]
    val c2 = pixels[width - 1]
    val c3 = pixels[(height - 1) * width]
    val c4 = pixels[(height - 1) * width + width - 1]

    val bgR = ((android.graphics.Color.red(c1) + android.graphics.Color.red(c2) + android.graphics.Color.red(c3) + android.graphics.Color.red(c4)) / 4)
    val bgG = ((android.graphics.Color.green(c1) + android.graphics.Color.green(c2) + android.graphics.Color.green(c3) + android.graphics.Color.green(c4)) / 4)
    val bgB = ((android.graphics.Color.blue(c1) + android.graphics.Color.blue(c2) + android.graphics.Color.blue(c3) + android.graphics.Color.blue(c4)) / 4)

    val threshold = (toleranceVal * 2.5f).toInt()

    for (i in pixels.indices) {
        val pixel = pixels[i]
        val pr = android.graphics.Color.red(pixel)
        val pg = android.graphics.Color.green(pixel)
        val pb = android.graphics.Color.blue(pixel)

        val diff = abs(pr - bgR) + abs(pg - bgG) + abs(pb - bgB)
        if (diff < threshold) {
            pixels[i] = when (mode) {
                BgReplaceMode.TRANSPARENT -> 0x00000000
                BgReplaceMode.SOLID_WHITE -> 0xFFFFFFFF.toInt()
                BgReplaceMode.NEON_GRADIENT -> 0xFF8B5CF6.toInt()
                BgReplaceMode.DARK_MODE -> 0xFF0B1326.toInt()
                BgReplaceMode.CYAN_VIBE -> 0xFF22D3EE.toInt()
            }
        }
    }

    outBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return outBitmap
}
