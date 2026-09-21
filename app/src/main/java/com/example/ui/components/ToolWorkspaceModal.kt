package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.ToolItem
import com.example.ui.theme.*
import com.example.ui.tools.*

@Composable
fun ToolWorkspaceModal(
    tool: ToolItem,
    onDismiss: () -> Unit,
    onCopyFeedback: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, BorderOutline.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .testTag("tool_workspace_dialog"),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceContainerLow.copy(alpha = 0.98f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SurfaceContainerHighest)
                                    .border(1.dp, BorderOutline.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getToolIcon(tool.iconName),
                                    contentDescription = tool.title,
                                    tint = tool.primaryColor,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Collection of Ram •",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = AccentCyan
                                    )
                                    Text(
                                        text = tool.categoryLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = tool.primaryColor,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(tool.primaryColor.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = tool.title,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontFamily = SoraFontFamily,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = TextPrimary
                                )
                                Text(
                                    text = tool.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceContainerHighest)
                                .testTag("close_workspace_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = BorderOutline.copy(alpha = 0.3f))

                    // Workspace Content
                    when (tool.id) {
                        "age" -> AgeCalculatorWorkspace(onCopyText = onCopyFeedback)
                        "units" -> UnitConverterWorkspace(onCopyText = onCopyFeedback)
                        "qr" -> QrGeneratorWorkspace(onCopyText = onCopyFeedback)
                        "compressor" -> ImageCompressorWorkspace(onCopyText = onCopyFeedback)
                        "password" -> PasswordGeneratorWorkspace(onCopyText = onCopyFeedback)
                        "splitter" -> ExpenseSplitterWorkspace(onCopyText = onCopyFeedback)
                        "palette" -> PaletteGeneratorWorkspace(onCopyText = onCopyFeedback)
                        "words" -> WordCounterWorkspace(onCopyText = onCopyFeedback)
                        "love" -> LoveCalculatorWorkspace(onCopyText = onCopyFeedback)
                        "bg_remover" -> PhotoBgRemoverWorkspace(onCopyText = onCopyFeedback)
                        "captions" -> CaptionGeneratorWorkspace(onCopyText = onCopyFeedback)
                        "thumbnail" -> ThumbnailGeneratorWorkspace(onCopyText = onCopyFeedback)
                        "tweet_meme" -> TweetMemeGeneratorWorkspace(onCopyText = onCopyFeedback)
                        "fancy_fonts" -> FancyBioFontWorkspace(onCopyText = onCopyFeedback)
                        "spin_wheel" -> SpinWheelWorkspace(onCopyText = onCopyFeedback)
                        "reel_hooks" -> ReelHookGeneratorWorkspace(onCopyText = onCopyFeedback)
                        "wallpaper" -> GradientWallpaperWorkspace(onCopyText = onCopyFeedback)
                        "aura" -> AuraReaderWorkspace(onCopyText = onCopyFeedback)
                        "ram_chat" -> RamAiChatbotWorkspace(onCopyText = onCopyFeedback)
                        "bmi" -> BmiHealthWorkspace(onCopyText = onCopyFeedback)
                        "water" -> WaterTrackerWorkspace(onCopyText = onCopyFeedback)
                        "sleep" -> SleepCycleWorkspace(onCopyText = onCopyFeedback)
                        "pomodoro" -> PomodoroTimerWorkspace(onCopyText = onCopyFeedback)
                        else -> {
                            Text("Tool workspace under preparation", color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}
