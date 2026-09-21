package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.TextFields
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

data class FontStyleVariation(
    val styleName: String,
    val sampleTransform: (String) -> String
)

@Composable
fun FancyBioFontWorkspace(
    onCopyText: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("Collection of Ram") }

    val fontStyles = remember {
        listOf(
            FontStyleVariation("𝕲𝖔𝖙𝖍𝖎𝖈 / 𝕱𝖗𝖆𝖐𝖙𝖚𝖗") { text -> toGothic(text) },
            FontStyleVariation("𝒞𝓊𝓇𝓈𝒾𝓋ℯ 𝒮𝒸𝓇𝒾𝓅𝓉") { text -> toScript(text) },
            FontStyleVariation("𝗕𝗼𝗹𝗱 𝗦𝗮𝗻𝘀") { text -> toBoldSans(text) },
            FontStyleVariation("𝘐𝘵𝘢𝘭𝘪𝘤 𝘚𝘢𝘯𝘴") { text -> toItalicSans(text) },
            FontStyleVariation("𝙼𝚘𝚗𝚘𝚜𝚙𝚊𝚌𝚎 𝚃𝚎𝚌𝚑") { text -> toMonospace(text) },
            FontStyleVariation("Ｓｐａｃｅｄ　Ｆｕｌｌｗｉｄｔｈ") { text -> toFullwidth(text) },
            FontStyleVariation("ꜱᴍᴀʟʟ ᴄᴀᴘꜱ ᴍɪɴɪᴍᴀʟ") { text -> toSmallCaps(text) },
            FontStyleVariation("Ⓒⓘⓡⓒⓛⓔⓓ Ⓑⓤⓑⓑⓛⓔ") { text -> toCircled(text) },
            FontStyleVariation("🅂🅀🅄🄰2🄴🄳") { text -> toSquared(text) },
            FontStyleVariation("uʍop ǝpısd∩ (Upside Down)") { text -> toUpsideDown(text) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input text field
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.7f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.TextFields, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                    Text(
                        text = "Type text to generate Bio & Caption fonts",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Enter your bio, username or aesthetic quote...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("fancy_font_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(
                    text = "Tap any style below to instantly copy it for Instagram, WhatsApp & TikTok!",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        // Style list
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            val target = inputText.ifEmpty { "Collection of Ram" }
            fontStyles.forEach { style ->
                val transformed = style.sampleTransform(target)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCopyText(transformed) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = style.styleName,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AccentCyan
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = transformed,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 17.sp
                                ),
                                color = TextPrimary
                            )
                        }

                        Button(
                            onClick = { onCopyText(transformed) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceContainerHighest,
                                contentColor = TextPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

// Unicode transformation mappers
private fun toGothic(text: String): String {
    return text.map { ch ->
        when (ch) {
            in 'a'..'z' -> String(Character.toChars(0x1D586 + (ch - 'a')))
            in 'A'..'Z' -> String(Character.toChars(0x1D56C + (ch - 'A')))
            else -> ch.toString()
        }
    }.joinToString("")
}

private fun toScript(text: String): String {
    return text.map { ch ->
        when (ch) {
            in 'a'..'z' -> String(Character.toChars(0x1D4EA + (ch - 'a')))
            in 'A'..'Z' -> String(Character.toChars(0x1D4D0 + (ch - 'A')))
            else -> ch.toString()
        }
    }.joinToString("")
}

private fun toBoldSans(text: String): String {
    return text.map { ch ->
        when (ch) {
            in 'a'..'z' -> String(Character.toChars(0x1D5EE + (ch - 'a')))
            in 'A'..'Z' -> String(Character.toChars(0x1D5D4 + (ch - 'A')))
            in '0'..'9' -> String(Character.toChars(0x1D7CE + (ch - '0')))
            else -> ch.toString()
        }
    }.joinToString("")
}

private fun toItalicSans(text: String): String {
    return text.map { ch ->
        when (ch) {
            in 'a'..'z' -> String(Character.toChars(0x1D622 + (ch - 'a')))
            in 'A'..'Z' -> String(Character.toChars(0x1D608 + (ch - 'A')))
            else -> ch.toString()
        }
    }.joinToString("")
}

private fun toMonospace(text: String): String {
    return text.map { ch ->
        when (ch) {
            in 'a'..'z' -> String(Character.toChars(0x1D68A + (ch - 'a')))
            in 'A'..'Z' -> String(Character.toChars(0x1D670 + (ch - 'A')))
            in '0'..'9' -> String(Character.toChars(0x1D7F6 + (ch - '0')))
            else -> ch.toString()
        }
    }.joinToString("")
}

private fun toFullwidth(text: String): String {
    return text.map { ch ->
        when (ch) {
            in '!'..'~' -> String(Character.toChars(ch.code + 0xFEE0))
            ' ' -> "　"
            else -> ch.toString()
        }
    }.joinToString("")
}

private fun toSmallCaps(text: String): String {
    val map = mapOf(
        'a' to "ᴀ", 'b' to "ʙ", 'c' to "ᴄ", 'd' to "ᴅ", 'e' to "ᴇ", 'f' to "ꜰ",
        'g' to "ɢ", 'h' to "ʜ", 'i' to "ɪ", 'j' to "ᴊ", 'k' to "ᴋ", 'l' to "ʟ",
        'm' to "ᴍ", 'n' to "ɴ", 'o' to "ᴏ", 'p' to "ᴘ", 'q' to "ǫ", 'r' to "ʀ",
        's' to "ꜱ", 't' to "ᴛ", 'u' to "ᴜ", 'v' to "ᴠ", 'w' to "ᴡ", 'x' to "x",
        'y' to "ʏ", 'z' to "ᴢ"
    )
    return text.lowercase().map { map[it] ?: it.toString() }.joinToString("")
}

private fun toCircled(text: String): String {
    return text.map { ch ->
        when (ch) {
            in 'a'..'z' -> String(Character.toChars(0x24D0 + (ch - 'a')))
            in 'A'..'Z' -> String(Character.toChars(0x24B6 + (ch - 'A')))
            in '1'..'9' -> String(Character.toChars(0x2460 + (ch - '1')))
            '0' -> "⓪"
            else -> ch.toString()
        }
    }.joinToString("")
}

private fun toSquared(text: String): String {
    return text.uppercase().map { ch ->
        when (ch) {
            in 'A'..'Z' -> String(Character.toChars(0x1F130 + (ch - 'A')))
            else -> ch.toString()
        }
    }.joinToString("")
}

private fun toUpsideDown(text: String): String {
    val normal = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789?,!."
    val flipped = "ɐqɔpǝɟɓɥıɾʞןɯuodbɹsʇnʌʍxʎz∀ꓭƆᗡƎℲ⅁HIſʞꞀWNOԀÒᴚS⊥∩ΛMX⅄Z0ƖᄅƐㄣϛ9ㄥ86¿'¡˙"
    return text.reversed().map { ch ->
        val idx = normal.indexOf(ch)
        if (idx != -1) flipped[idx] else ch
    }.joinToString("")
}
