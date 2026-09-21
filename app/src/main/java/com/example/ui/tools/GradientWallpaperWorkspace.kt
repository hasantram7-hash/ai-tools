package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.random.Random

data class WallpaperPalette(
    val name: String,
    val colors: List<Color>,
    val moodTag: String
)

@Composable
fun GradientWallpaperWorkspace(
    onCopyText: (String) -> Unit
) {
    val presets = remember {
        listOf(
            WallpaperPalette("Aurora Borealis", listOf(Color(0xFF0F172A), Color(0xFF06B6D4), Color(0xFF10B981)), "Deep Cyber"),
            WallpaperPalette("Neon Sunset", listOf(Color(0xFF1E1B4B), Color(0xFF8B5CF6), Color(0xFFF43F5E)), "Synthwave"),
            WallpaperPalette("Minimal Obsidian", listOf(Color(0xFF000000), Color(0xFF1E293B), Color(0xFF334155)), "Stealth"),
            WallpaperPalette("Cotton Candy", listOf(Color(0xFFFDE047), Color(0xFFF472B6), Color(0xFF818CF8)), "Pastel Aesthetic"),
            WallpaperPalette("Matrix Emerald", listOf(Color(0xFF022C22), Color(0xFF059669), Color(0xFF34D399)), "Hacker Green"),
            WallpaperPalette("Cosmic Nebula", listOf(Color(0xFF18181B), Color(0xFF4C1D95), Color(0xFFE11D48)), "Galaxy")
        )
    }

    var selectedPaletteIndex by remember { mutableIntStateOf(0) }
    var currentPalette by remember { mutableStateOf(presets[0]) }

    fun randomizeColors() {
        val randStart = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
        val randMid = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
        val randEnd = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
        currentPalette = WallpaperPalette("Custom Vibe #${Random.nextInt(100, 999)}", listOf(randStart, randMid, randEnd), "Dynamic AI")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Phone Wallpaper 9:16 Aspect Preview
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(2.dp, BorderOutline.copy(alpha = 0.4f)),
            modifier = Modifier
                .width(220.dp)
                .aspectRatio(9f / 16f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(currentPalette.colors)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Mock phone clock
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 28.dp)
                ) {
                    Text(
                        text = "09:41",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Monday, September 21",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.8f))
                    )
                }

                // Center aesthetic mood pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.35f),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = currentPalette.moodTag.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = { randomizeColors() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceContainerHighest,
                    contentColor = TextPrimary
                )
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Randomize")
            }

            Button(
                onClick = {
                    val hexList = currentPalette.colors.joinToString(", ") { c ->
                        String.format("#%06X", 0xFFFFFF and c.hashCode())
                    }
                    val spec = "Aesthetic Wallpaper: \"${currentPalette.name}\" [${currentPalette.moodTag}]. Hex Palette: $hexList. Made with Collection of Ram."
                    onCopyText(spec)
                },
                modifier = Modifier.weight(1.2f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentCyan,
                    contentColor = Color(0xFF003840)
                )
            ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy Palette")
            }
        }

        // Presets Carousel
        Text("Trending Aesthetic Themes", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.take(4).forEachIndexed { idx, p ->
                val isSel = currentPalette.name == p.name
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.verticalGradient(p.colors))
                        .border(
                            if (isSel) 2.5.dp else 1.dp,
                            if (isSel) Color.White else BorderOutline.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            currentPalette = p
                        }
                )
            }
        }
    }
}
