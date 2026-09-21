package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ToolCategory
import com.example.model.ToolItem
import com.example.model.ToolRepository
import com.example.ui.components.FaqSection
import com.example.ui.components.ToolCard
import com.example.ui.components.ToolWorkspaceModal
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionOfRamApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ToolCategory.ALL) }
    var activeToolModal by remember { mutableStateOf<ToolItem?>(null) }

    // Toast feedback notification
    var toastMessage by remember { mutableStateOf<String?>(null) }

    fun showToast(message: String) {
        toastMessage = message
        coroutineScope.launch {
            delay(2400)
            if (toastMessage == message) {
                toastMessage = null
            }
        }
    }

    fun copyToClipboard(text: String, feedback: String = "Copied to clipboard!") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Collection of Ram", text)
        clipboard.setPrimaryClip(clip)
        showToast(feedback)
    }

    fun launchRandomTool() {
        val randomTool = ToolRepository.tools.random()
        activeToolModal = randomTool
    }

    // Filter tools
    val filteredTools = remember(searchQuery, selectedCategory) {
        ToolRepository.tools.filter { tool ->
            val matchesCategory = (selectedCategory == ToolCategory.ALL) || (tool.category == selectedCategory)
            val query = searchQuery.trim().lowercase()
            val matchesSearch = query.isEmpty() ||
                    tool.title.lowercase().contains(query) ||
                    tool.description.lowercase().contains(query) ||
                    tool.id.lowercase().contains(query) ||
                    tool.categoryLabel.lowercase().contains(query)
            matchesCategory && matchesSearch
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        // Ambient background glow nodes
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = (-40).dp, y = (-20).dp)
                .clip(CircleShape)
                .background(AccentPurple.copy(alpha = 0.12f))
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = 180.dp)
                .clip(CircleShape)
                .background(AccentCyan.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomStart)
                .offset(x = 40.dp, y = 40.dp)
                .clip(CircleShape)
                .background(AccentPink.copy(alpha = 0.08f))
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                RamHeader(
                    onRandomTool = { launchRandomTool() },
                    onSelectToolKey = { key ->
                        val target = ToolRepository.tools.firstOrNull { it.id == key }
                        if (target != null) activeToolModal = target
                    }
                )
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = 320.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding() + 8.dp,
                    bottom = 48.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // HERO SECTION
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HeroSection(
                        onExploreTools = {
                            coroutineScope.launch {
                                gridState.animateScrollToItem(1)
                            }
                        },
                        onRandomTool = { launchRandomTool() }
                    )
                }

                // SEARCH & FILTER STATION
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SearchAndFilterSection(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        selectedCategory = selectedCategory,
                        onCategorySelect = { selectedCategory = it },
                        totalVisible = filteredTools.size,
                        onQuickLaunch = { toolId ->
                            val tool = ToolRepository.tools.firstOrNull { it.id == toolId }
                            if (tool != null) activeToolModal = tool
                        }
                    )
                }

                // 8 TOOLS GRID
                if (filteredTools.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        NoResultsBox(
                            onReset = {
                                searchQuery = ""
                                selectedCategory = ToolCategory.ALL
                            }
                        )
                    }
                } else {
                    items(filteredTools, key = { it.id }) { tool ->
                        ToolCard(
                            tool = tool,
                            onLaunch = { activeToolModal = it }
                        )
                    }
                }

                // FAQ ACCORDION SECTION
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(16.dp))
                    FaqSection()
                }

                // FOOTER
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(24.dp))
                    RamFooter(
                        onSelectToolKey = { key ->
                            val target = ToolRepository.tools.firstOrNull { it.id == key }
                            if (target != null) activeToolModal = target
                        }
                    )
                }
            }
        }

        // Dedicated Tool Workspace Dialog
        activeToolModal?.let { tool ->
            ToolWorkspaceModal(
                tool = tool,
                onDismiss = { activeToolModal = null },
                onCopyFeedback = { message ->
                    copyToClipboard(message, message)
                }
            )
        }

        // Floating Toast Notification
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.95f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = toastMessage ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun RamHeader(
    onRandomTool: () -> Unit,
    onSelectToolKey: (String) -> Unit
) {
    Surface(
        color = BgDark.copy(alpha = 0.85f),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderOutline.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Subtitle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Neon glowing 'R' avatar badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(AccentPurple, AccentCyan)
                            )
                        )
                        .padding(1.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(SurfaceContainerLowest),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "R",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = SoraFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = Color.White
                        )
                    }
                }

                Column {
                    Text(
                        text = "Collection of Ram",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = SoraFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                    Text(
                        text = "Simple tools. Beautifully crafted.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Right header actions: Casino Random Tool + CR Avatar badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onRandomTool,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceContainerHigh)
                        .testTag("random_tool_header_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = "Random Tool",
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // CR Initials Pill
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(AccentPurple, AccentCyan))
                        )
                        .padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(SurfaceContainerLowest),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CR",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeroSection(
    onExploreTools: () -> Unit,
    onRandomTool: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Eyebrow Super-Pill
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(SurfaceContainerHigh.copy(alpha = 0.6f))
                .border(1.dp, BorderOutline.copy(alpha = 0.3f), CircleShape)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AccentCyan)
            )
            Text(
                text = "COLLECTION OF RAM",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = AccentCyan
            )
            Text(
                text = "100% Free & Private",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SurfaceContainerHighest)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        // Hero headline with gradient
        Text(
            text = "Free Online Tools",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = SoraFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 44.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        // Subtitle
        Text(
            text = "Simple, fast, and beautifully crafted tools for everyday tasks — computed entirely inside your sandbox with zero latency.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        // Hero Action Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Button(
                onClick = onExploreTools,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentPurple,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
                modifier = Modifier.testTag("explore_tools_button")
            ) {
                Text(
                    text = "Explore Tools",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            OutlinedButton(
                onClick = onRandomTool,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = SurfaceContainerHigh.copy(alpha = 0.7f),
                    contentColor = TextPrimary
                ),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
                modifier = Modifier.testTag("hero_random_tool_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = null,
                    tint = AccentCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Random Tool",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
fun SearchAndFilterSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCategory: ToolCategory,
    onCategorySelect: (ToolCategory) -> Unit,
    totalVisible: Int,
    onQuickLaunch: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search Input Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tool_search_input"),
            placeholder = {
                Text(
                    "Search 8 instant tools (e.g., 'password', 'compress', 'qr', 'palette')...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = AccentPurple,
                    modifier = Modifier.size(22.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentPurple.copy(alpha = 0.7f),
                unfocusedBorderColor = BorderOutline.copy(alpha = 0.4f),
                focusedContainerColor = SurfaceContainerLow.copy(alpha = 0.8f),
                unfocusedContainerColor = SurfaceContainerLow.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
        )

        // Category Pills row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ToolCategory.entries.forEach { category ->
                val isSelected = selectedCategory == category
                val label = when (category) {
                    ToolCategory.ALL -> "All (8)"
                    else -> category.displayName
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) AccentPurple else SurfaceContainerHigh.copy(alpha = 0.7f))
                        .border(
                            1.dp,
                            if (isSelected) AccentPurple else BorderOutline.copy(alpha = 0.3f),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onCategorySelect(category) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("category_pill_${category.name}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isSelected) Color.White else TextSecondary
                    )
                }
            }
        }

        // Meta status counter & Popular chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = AccentCyan,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Showing $totalVisible of 8 utilities",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }

        // Popular Chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "POPULAR:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextMuted
            )

            QuickChip(label = "Password Gen", icon = Icons.Default.VpnKey, tint = AccentPurple) {
                onQuickLaunch("password")
            }
            QuickChip(label = "QR Matrix", icon = Icons.Default.QrCode, tint = AccentCyan) {
                onQuickLaunch("qr")
            }
            QuickChip(label = "Palette Studio", icon = Icons.Default.Palette, tint = AccentPink) {
                onQuickLaunch("palette")
            }
            QuickChip(label = "Image Shrinker", icon = Icons.Default.Compress, tint = AccentPurple) {
                onQuickLaunch("compressor")
            }
        }
    }
}

@Composable
fun QuickChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainer.copy(alpha = 0.7f))
            .border(1.dp, BorderOutline.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextPrimary)
    }
}

@Composable
fun NoResultsBox(onReset: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow.copy(alpha = 0.7f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterNone,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "No matching utility found",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
            Text(
                text = "Try refining your keyword query or reset the category filter tab.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onReset,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Text("Reset Filters", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun RamFooter(onSelectToolKey: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.95f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header in footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(AccentPurple.copy(alpha = 0.3f))
                        .border(1.dp, AccentPurple, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("R", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Text(
                    text = "Collection of Ram",
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = SoraFontFamily),
                    color = TextPrimary
                )
            }

            Text(
                text = "Simple tools. Beautifully crafted. Built with hyper-focused privacy, zero latency, and pure client compute.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AccentPurple.copy(alpha = 0.15f))
                    .border(1.dp, AccentPurple.copy(alpha = 0.25f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(AccentCyan)
                    )
                    Text(
                        text = "100% Client-side & Private",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentPurple
                    )
                }
            }

            HorizontalDivider(color = BorderOutline.copy(alpha = 0.2f))

            // Featured Utilities shortcuts
            Text(
                text = "FEATURED UTILITIES",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextMuted
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip(
                    onClick = { onSelectToolKey("password") },
                    label = { Text("Password", color = TextPrimary) }
                )
                SuggestionChip(
                    onClick = { onSelectToolKey("qr") },
                    label = { Text("QR Matrix", color = TextPrimary) }
                )
                SuggestionChip(
                    onClick = { onSelectToolKey("compressor") },
                    label = { Text("Compressor", color = TextPrimary) }
                )
                SuggestionChip(
                    onClick = { onSelectToolKey("palette") },
                    label = { Text("Palette", color = TextPrimary) }
                )
            }

            HorizontalDivider(color = BorderOutline.copy(alpha = 0.2f))

            // Bottom signature
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Zero telemetry. Zero network transmission.",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }

                Text(
                    text = "© 2026 Collection of Ram",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
    }
}
