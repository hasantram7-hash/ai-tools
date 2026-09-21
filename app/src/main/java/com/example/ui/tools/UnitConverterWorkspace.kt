package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

enum class UnitCategory(val label: String) {
    LENGTH("Length"),
    WEIGHT("Weight"),
    TEMPERATURE("Temperature"),
    AREA("Area"),
    VOLUME("Volume")
}

@Composable
fun UnitConverterWorkspace(
    onCopyText: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(UnitCategory.LENGTH) }

    val unitsForCategory = remember(selectedCategory) {
        when (selectedCategory) {
            UnitCategory.LENGTH -> listOf("Meters", "Kilometers", "Feet", "Inches", "Miles", "Centimeters")
            UnitCategory.WEIGHT -> listOf("Kilograms", "Grams", "Pounds", "Ounces", "Metric Tons")
            UnitCategory.TEMPERATURE -> listOf("Celsius", "Fahrenheit", "Kelvin")
            UnitCategory.AREA -> listOf("Sq Meters", "Sq Kilometers", "Sq Feet", "Acres", "Hectares")
            UnitCategory.VOLUME -> listOf("Liters", "Milliliters", "Gallons (US)", "Cups", "Cubic Meters")
        }
    }

    var fromUnit by remember(selectedCategory) { mutableStateOf(unitsForCategory.first()) }
    var toUnit by remember(selectedCategory) {
        mutableStateOf(if (unitsForCategory.size > 1) unitsForCategory[1] else unitsForCategory.first())
    }
    var inputValueStr by remember { mutableStateOf("100") }

    val convertedValue = remember(inputValueStr, fromUnit, toUnit, selectedCategory) {
        val input = inputValueStr.toDoubleOrNull() ?: 0.0
        convertUnits(selectedCategory, fromUnit, toUnit, input)
    }

    val formulaText = "$inputValueStr $fromUnit = $convertedValue $toUnit"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Category Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            UnitCategory.entries.take(4).forEach { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) AccentCyan else SurfaceContainerHighest)
                        .clickable {
                            selectedCategory = cat
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat.label,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isSelected) BgDark else TextSecondary
                    )
                }
            }
        }

        // Conversion Boxes
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // "From" Input Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("FROM", style = MaterialTheme.typography.labelSmall, color = TextMuted)

                    OutlinedTextField(
                        value = inputValueStr,
                        onValueChange = { inputValueStr = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentCyan,
                            unfocusedBorderColor = BorderOutline,
                            focusedContainerColor = SurfaceContainerLowest,
                            unfocusedContainerColor = SurfaceContainerLowest
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    UnitDropdownSelector(
                        selected = fromUnit,
                        options = unitsForCategory,
                        onSelect = { fromUnit = it }
                    )
                }
            }

            // "To" Output Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("TO (COMPUTED)", style = MaterialTheme.typography.labelSmall, color = TextMuted)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLowest)
                            .border(1.dp, BorderOutline, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = convertedValue,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = AccentCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    UnitDropdownSelector(
                        selected = toUnit,
                        options = unitsForCategory,
                        onSelect = { toUnit = it }
                    )
                }
            }
        }

        // Formula readout card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.7f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formulaText,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = AccentPurple,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = { onCopyText(formulaText) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy", color = AccentCyan, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
fun UnitDropdownSelector(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerHighest)
                .border(1.dp, BorderOutline.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selected, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = TextMuted)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(SurfaceContainerHigh)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = TextPrimary) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun convertUnits(category: UnitCategory, from: String, to: String, value: Double): String {
    if (from == to) return formatDecimals(value)

    return when (category) {
        UnitCategory.TEMPERATURE -> {
            var c = value
            if (from == "Fahrenheit") c = (value - 32.0) * (5.0 / 9.0)
            if (from == "Kelvin") c = value - 273.15

            val res = when (to) {
                "Celsius" -> c
                "Fahrenheit" -> (c * (9.0 / 5.0)) + 32.0
                "Kelvin" -> c + 273.15
                else -> c
            }
            formatDecimals(res)
        }
        UnitCategory.LENGTH -> {
            val toBaseMeters = mapOf(
                "Meters" to 1.0,
                "Kilometers" to 1000.0,
                "Centimeters" to 0.01,
                "Feet" to 0.3048,
                "Inches" to 0.0254,
                "Miles" to 1609.34
            )
            val base = value * (toBaseMeters[from] ?: 1.0)
            val res = base / (toBaseMeters[to] ?: 1.0)
            formatDecimals(res)
        }
        UnitCategory.WEIGHT -> {
            val toBaseKg = mapOf(
                "Kilograms" to 1.0,
                "Grams" to 0.001,
                "Pounds" to 0.453592,
                "Ounces" to 0.0283495,
                "Metric Tons" to 1000.0
            )
            val base = value * (toBaseKg[from] ?: 1.0)
            val res = base / (toBaseKg[to] ?: 1.0)
            formatDecimals(res)
        }
        UnitCategory.AREA -> {
            val toBaseSqM = mapOf(
                "Sq Meters" to 1.0,
                "Sq Kilometers" to 1000000.0,
                "Sq Feet" to 0.092903,
                "Acres" to 4046.86,
                "Hectares" to 10000.0
            )
            val base = value * (toBaseSqM[from] ?: 1.0)
            val res = base / (toBaseSqM[to] ?: 1.0)
            formatDecimals(res)
        }
        UnitCategory.VOLUME -> {
            val toBaseLiters = mapOf(
                "Liters" to 1.0,
                "Milliliters" to 0.001,
                "Gallons (US)" to 3.78541,
                "Cups" to 0.236588,
                "Cubic Meters" to 1000.0
            )
            val base = value * (toBaseLiters[from] ?: 1.0)
            val res = base / (toBaseLiters[to] ?: 1.0)
            formatDecimals(res)
        }
    }
}

private fun formatDecimals(num: Double): String {
    return if (num % 1.0 == 0.0 && num < 1000000) {
        num.toLong().toString()
    } else {
        "%.4f".format(num).trimEnd('0').trimEnd('.')
    }
}
