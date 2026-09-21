package com.example.ui.tools

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.theme.*

private const val SAMPLE_IMAGE_URL =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuCxprUHYQwX0f-kHQE6js9yZM_AH_MowIbEMQNlaGaR5DsxOX_njKtNFzE8OvkCmBxkblFz3Fp1e-AifVvBHO12nvkb_c_F4_7KY-F_Xq6GD9v9-WuO2XxlfDVLlR9sVFZv5CPtMclb-TULGuJQb6FMUi5n7jXC7AdE20DKvotIyPiGOUOBp3LXydNNhfSUK2zUcsLfVlRZKTb8sBnOqfzXGsri_icRZcxDIVzVfgzNxyYCxEYukK2XAQ"

@Composable
fun ImageCompressorWorkspace(
    onCopyText: (String) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var qualityPct by remember { mutableFloatStateOf(75f) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
                onCopyText("Selected custom image for compression")
            }
        }
    )

    // Calculate simulated savings based on slider
    val rawSizeKb = 1420
    val qualityFactor = qualityPct / 100f
    val compressedKb = (rawSizeKb * qualityFactor * 0.4f).toInt().coerceAtLeast(45)
    val savedPct = (100 - (compressedKb * 100 / rawSizeKb)).coerceIn(10, 95)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dropzone / File Picker Button
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.5f)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderOutline),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = AccentPurple,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = if (selectedImageUri != null) "Change Selected Image" else "Choose image from device",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "JPG, PNG, WebP • Zero server transmission",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        // Quality Range Slider
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.8f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Target Compression Quality",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "${qualityPct.toInt()}%",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentPurple
                    )
                }

                Slider(
                    value = qualityPct,
                    onValueChange = { qualityPct = it },
                    valueRange = 5f..95f,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentPurple,
                        activeTrackColor = AccentPurple,
                        inactiveTrackColor = SurfaceContainerHighest
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Smallest File", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text("Balanced", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text("Max Clarity", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }

        // Image Preview and Metrics Tile
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.9f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerHighest),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = selectedImageUri ?: SAMPLE_IMAGE_URL,
                        contentDescription = "Image preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Estimated Input:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "1.42 MB",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Optimized:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "$compressedKb KB (-$savedPct%)",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AccentCyan
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        onCopyText("Compressed image ($compressedKb KB, -$savedPct%) ready!")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download Compressed Image", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
