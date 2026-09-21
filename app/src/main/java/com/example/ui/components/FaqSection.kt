package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

data class FaqItem(
    val question: String,
    val answer: String
)

@Composable
fun FaqSection(modifier: Modifier = Modifier) {
    val faqs = remember {
        listOf(
            FaqItem(
                question = "Are my uploaded images and passwords sent over the internet?",
                answer = "Never. Collection of Ram uses local client-side processing including Canvas 2D and cryptographic randomness. Zero files, passwords, or text snippets leave your device or hit an external server. You can even disconnect your internet and every single tool continues to function without degradation."
            ),
            FaqItem(
                question = "Is the Collection of Ram utility suite completely free?",
                answer = "Yes. Collection of Ram is completely free, ad-free, and requires zero account registrations, cookie consent banners, or subscriptions. It was architected as an atmospheric, frictionless utility workstation for creators and engineers."
            ),
            FaqItem(
                question = "How does client-side image compression achieve 80%+ savings?",
                answer = "Your image is loaded into an off-screen bitmap, dynamically resampled via progressive interpolation, and serialized to WebP or JPEG formats through native encoding buffers. This strips heavy EXIF metadata and optimizes macroblocks locally."
            )
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ZERO COMPROMISE PRIVACY",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AccentCyan
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.headlineSmall.copy(fontFamily = SoraFontFamily),
                color = TextPrimary
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            faqs.forEach { faq ->
                FaqAccordionItem(faq = faq)
            }
        }
    }
}

@Composable
fun FaqAccordionItem(faq: FaqItem) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderOutline.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerLow.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = faq.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                }
            }
        }
    }
}
