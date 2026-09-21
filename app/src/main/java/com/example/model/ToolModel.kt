package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPink
import com.example.ui.theme.AccentPurple

enum class ToolCategory(val displayName: String) {
    ALL("All"),
    CALCULATORS("Calculators"),
    GENERATORS("Generators"),
    MEDIA_TEXT("Media & Text"),
    UTILITIES("Utilities")
}

data class ToolItem(
    val id: String,
    val title: String,
    val category: ToolCategory,
    val categoryLabel: String,
    val iconName: String,
    val description: String,
    val metricLabel: String,
    val metricValue: String,
    val statusBadge: String,
    val tagText: String,
    val primaryColor: Color,
    val secondaryColor: Color
)

object ToolRepository {
    val tools = listOf(
        ToolItem(
            id = "age",
            title = "Age Calculator",
            category = ToolCategory.CALCULATORS,
            categoryLabel = "Calculator",
            iconName = "calendar_clock",
            description = "Calculate exact years, months, days lived, total hours, and next birthday countdown.",
            metricLabel = "Precision",
            metricValue = "Real-time sec",
            statusBadge = "Live Milestone",
            tagText = "v2.1 • Instant",
            primaryColor = AccentPurple,
            secondaryColor = AccentCyan
        ),
        ToolItem(
            id = "units",
            title = "Unit Converter",
            category = ToolCategory.CALCULATORS,
            categoryLabel = "Calculator",
            iconName = "sync_alt",
            description = "Real-time conversion across Length, Weight, Temperature, Area, and Volume metrics.",
            metricLabel = "Metrics",
            metricValue = "5 Domains",
            statusBadge = "Bidirectional",
            tagText = "SI / Imperial",
            primaryColor = AccentCyan,
            secondaryColor = AccentPurple
        ),
        ToolItem(
            id = "qr",
            title = "QR Code Generator",
            category = ToolCategory.GENERATORS,
            categoryLabel = "Generator",
            iconName = "qr_code_scanner",
            description = "Generate instant downloadable SVG & PNG QR matrices with custom foreground accents.",
            metricLabel = "Export",
            metricValue = "High-Res PNG",
            statusBadge = "Zero Tracking",
            tagText = "Custom Color",
            primaryColor = AccentPink,
            secondaryColor = AccentPurple
        ),
        ToolItem(
            id = "compressor",
            title = "Image Compressor",
            category = ToolCategory.MEDIA_TEXT,
            categoryLabel = "Media & Text",
            iconName = "photo_size_select_small",
            description = "In-browser client-side image optimizer with variable quality slider and byte reduction metrics.",
            metricLabel = "Engine",
            metricValue = "HTML5 Canvas",
            statusBadge = "Up to -85%",
            tagText = "No Server Upload",
            primaryColor = AccentPurple,
            secondaryColor = AccentCyan
        ),
        ToolItem(
            id = "password",
            title = "Password Generator",
            category = ToolCategory.GENERATORS,
            categoryLabel = "Generators",
            iconName = "key",
            description = "Cryptographically secure password foundry with length constraints and live bit entropy score.",
            metricLabel = "RNG",
            metricValue = "crypto.getRandom",
            statusBadge = "128-bit+",
            tagText = "Custom Rules",
            primaryColor = AccentCyan,
            secondaryColor = AccentPink
        ),
        ToolItem(
            id = "splitter",
            title = "Expense Splitter",
            category = ToolCategory.CALCULATORS,
            categoryLabel = "Calculators",
            iconName = "receipt_long",
            description = "Split dinner checks, calculate tip percentages, adjust party counts, and export individual tallies.",
            metricLabel = "Features",
            metricValue = "Tip + Even Split",
            statusBadge = "Quick Share",
            tagText = "Smart Rounding",
            primaryColor = AccentPurple,
            secondaryColor = AccentPink
        ),
        ToolItem(
            id = "palette",
            title = "Palette Generator",
            category = ToolCategory.GENERATORS,
            categoryLabel = "Generators",
            iconName = "palette",
            description = "Harmonious 5-color aesthetic palettes with lockable swatches, hex copy, and CSS variable export.",
            metricLabel = "Shortcut",
            metricValue = "Spacebar",
            statusBadge = "Export CSS",
            tagText = "Lockable Slugs",
            primaryColor = AccentPink,
            secondaryColor = AccentCyan
        ),
        ToolItem(
            id = "words",
            title = "Word Counter",
            category = ToolCategory.MEDIA_TEXT,
            categoryLabel = "Media & Text",
            iconName = "format_align_left",
            description = "Real-time word, character, sentence, paragraph counts, reading duration, and keyword distribution.",
            metricLabel = "Speed",
            metricValue = "220 wpm",
            statusBadge = "Live Frequency",
            tagText = "Keyword Density",
            primaryColor = AccentCyan,
            secondaryColor = AccentPurple
        )
    )
}
