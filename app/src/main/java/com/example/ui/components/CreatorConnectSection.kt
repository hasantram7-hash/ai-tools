package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class SocialLink(
    val id: String,
    val title: String,
    val handle: String,
    val url: String,
    val icon: ImageVector,
    val badgeColor: Color,
    val isEmail: Boolean = false
)

val CREATOR_LINKS = listOf(
    SocialLink(
        id = "github",
        title = "GitHub",
        handle = "hem-narayan",
        url = "https://github.com/hem-narayan",
        icon = Icons.Default.Code,
        badgeColor = AccentWhite
    ),
    SocialLink(
        id = "linkedin",
        title = "LinkedIn",
        handle = "ram-hem-7874b5384",
        url = "https://www.linkedin.com/in/ram-hem-7874b5384/",
        icon = Icons.Default.Work,
        badgeColor = Color(0xFF0A66C2)
    ),
    SocialLink(
        id = "youtube",
        title = "YouTube",
        handle = "@its_me_ram_07",
        url = "https://www.youtube.com/@its_me_ram_07",
        icon = Icons.Default.SmartDisplay,
        badgeColor = Color(0xFFFF0000)
    ),
    SocialLink(
        id = "tiktok",
        title = "TikTok",
        handle = "@ram____hem",
        url = "https://www.tiktok.com/@ram____hem",
        icon = Icons.Default.MusicNote,
        badgeColor = AccentRose
    ),
    SocialLink(
        id = "facebook",
        title = "Facebook",
        handle = "hemnarayan.78",
        url = "https://www.facebook.com/hemnarayan.78",
        icon = Icons.Default.Public,
        badgeColor = Color(0xFF1877F2)
    ),
    SocialLink(
        id = "email",
        title = "Email Direct",
        handle = "tg3300659@gmail.com",
        url = "mailto:tg3300659@gmail.com",
        icon = Icons.Default.Email,
        badgeColor = AccentPink,
        isEmail = true
    )
)

@Composable
fun CreatorConnectSection(
    onCopyFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun launchLink(link: SocialLink) {
        try {
            val intent = if (link.isEmail) {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:${link.handle}")
                }
            } else {
                Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            onCopyFeedback("Link: ${link.url}")
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        AccentPink.copy(alpha = 0.45f),
                        AccentWhite.copy(alpha = 0.2f),
                        AccentRose.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .testTag("creator_connect_section"),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.95f)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Eyebrow Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AccentPink.copy(alpha = 0.12f))
                        .border(1.dp, AccentPink.copy(alpha = 0.35f), CircleShape)
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(AccentPink)
                    )
                    Text(
                        text = "CREATOR & DEVELOPER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AccentRose
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceContainerHigh,
                    modifier = Modifier.clickable {
                        val allLinks = buildString {
                            appendLine("Connect with Hem Narayan (Ram):")
                            appendLine("• GitHub: https://github.com/hem-narayan")
                            appendLine("• LinkedIn: https://www.linkedin.com/in/ram-hem-7874b5384/")
                            appendLine("• YouTube: https://www.youtube.com/@its_me_ram_07")
                            appendLine("• TikTok: https://www.tiktok.com/@ram____hem")
                            appendLine("• Facebook: https://www.facebook.com/hemnarayan.78")
                            appendLine("• Email: tg3300659@gmail.com")
                        }
                        onCopyFeedback(allLinks)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = AccentWhite, modifier = Modifier.size(14.dp))
                        Text("Copy All", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = AccentWhite)
                    }
                }
            }

            // Creator Profile Highlight
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                SurfaceContainerLow,
                                SurfaceContainerLowest
                            )
                        )
                    )
                    .border(1.dp, BorderOutline.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Avatar with glowing pink-white gradient ring
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(AccentPink, AccentWhite, AccentRose, AccentPink)
                            )
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(BgDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "HN",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp
                            ),
                            color = AccentWhite
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Hem Narayan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified Creator",
                            tint = AccentPink,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "Ram • Lead Architect & Developer",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AccentRose
                    )

                    Text(
                        text = "Crafting high-speed private utilities, creative AI workflows & modern Android engines.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Grid of Social Cards (2-column layout)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                CREATOR_LINKS.chunked(2).forEach { rowLinks ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowLinks.forEach { link ->
                            SocialLinkCard(
                                link = link,
                                onClick = { launchLink(link) },
                                onCopy = {
                                    val copyContent = if (link.isEmail) link.handle else link.url
                                    onCopyFeedback(copyContent)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowLinks.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialLinkCard(
    link: SocialLink,
    onClick: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        link.badgeColor.copy(alpha = 0.35f),
                        BorderOutline.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() },
        color = SurfaceContainerLow,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(link.badgeColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = link.icon,
                        contentDescription = link.title,
                        tint = link.badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = link.title,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = link.handle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(
                onClick = onCopy,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Link",
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
