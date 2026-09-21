package com.example.ui.tools

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TweetMemeGeneratorWorkspace(
    onCopyText: (String) -> Unit
) {
    var displayName by remember { mutableStateOf("Ram Patel") }
    var handleName by remember { mutableStateOf("ram_official") }
    var tweetText by remember { mutableStateOf("The secret to high productivity isn't working 16 hours a day. It's eliminating distractions, protecting your focus, and having the right tools. ⚡") }
    var isVerified by remember { mutableStateOf(true) }
    var likesCount by remember { mutableStateOf("48.2K") }
    var repostsCount by remember { mutableStateOf("12.4K") }
    var timeStamp by remember { mutableStateOf("10:42 PM · Today") }
    var isCardDark by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Live Tweet / Post Preview Card
        val cardBg = if (isCardDark) Color(0xFF0F172A) else Color.White
        val cardBorder = if (isCardDark) Color(0xFF334155) else Color(0xFFE2E8F0)
        val textCol = if (isCardDark) Color.White else Color(0xFF0F172A)
        val subCol = if (isCardDark) Color(0xFF94A3B8) else Color(0xFF64748B)

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, cardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // User header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar circle
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(AccentPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayName.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = displayName.ifEmpty { "Name" },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = textCol
                            )
                            if (isVerified) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = Color(0xFF1D9BF0),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = "@${handleName.ifEmpty { "username" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = subCol
                        )
                    }

                    // Twitter / X Icon
                    Text(
                        text = "𝕏",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = textCol
                    )
                }

                // Tweet Body text
                Text(
                    text = tweetText.ifEmpty { "Your post text appears right here..." },
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp, lineHeight = 24.sp),
                    color = textCol
                )

                // Timestamp
                Text(
                    text = "$timeStamp · Twitter for Android",
                    style = MaterialTheme.typography.labelSmall,
                    color = subCol
                )

                HorizontalDivider(color = cardBorder.copy(alpha = 0.6f))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = repostsCount, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = textCol)
                            Text(text = "Reposts", style = MaterialTheme.typography.labelSmall, color = subCol)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = likesCount, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = textCol)
                            Text(text = "Likes", style = MaterialTheme.typography.labelSmall, color = subCol)
                        }
                    }

                    // Copy text button
                    Button(
                        onClick = {
                            val fullPost = "𝕏 @$handleName ($displayName):\n\"$tweetText\"\n\n❤️ $likesCount Likes · 🔁 $repostsCount Reposts"
                            onCopyText(fullPost)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCardDark) Color(0xFF1E293B) else Color(0xFFF1F5F9),
                            contentColor = textCol
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy Quote", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Controls
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.7f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Customize Post & Persona",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = handleName,
                        onValueChange = { handleName = it },
                        label = { Text("Handle (@)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = tweetText,
                    onValueChange = { tweetText = it },
                    label = { Text("Post / Tweet Content") },
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth().testTag("tweet_content_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = likesCount,
                        onValueChange = { likesCount = it },
                        label = { Text("Likes") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = repostsCount,
                        onValueChange = { repostsCount = it },
                        label = { Text("Reposts") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isVerified,
                            onCheckedChange = { isVerified = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1D9BF0))
                        )
                        Text("Blue Verified Badge", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isCardDark,
                            onCheckedChange = { isCardDark = it },
                            colors = CheckboxDefaults.colors(checkedColor = AccentPurple)
                        )
                        Text("Dark Mode Post", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }
                }
            }
        }
    }
}
