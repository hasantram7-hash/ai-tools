package com.example.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ExpenseSplitterWorkspace(
    onCopyText: (String) -> Unit
) {
    var billInputStr by remember { mutableStateOf("128.50") }
    var selectedTipPct by remember { mutableIntStateOf(15) }
    var peopleCount by remember { mutableIntStateOf(4) }

    val tipOptions = listOf(10, 15, 20, 25)

    val (totalWithTip, tipTotal, perPerson) = remember(billInputStr, selectedTipPct, peopleCount) {
        val bill = billInputStr.toDoubleOrNull() ?: 0.0
        val tip = bill * (selectedTipPct / 100.0)
        val total = bill + tip
        val perP = if (peopleCount > 0) total / peopleCount else total
        Triple(total, tip, perP)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bill Input and Parameters Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Total Bill Amount ($)",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                OutlinedTextField(
                    value = billInputStr,
                    onValueChange = { billInputStr = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = JetBrainsMonoFamily,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = BorderOutline,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Tip Gratuity
                Text(
                    text = "Tip Gratuity",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tipOptions.forEach { tip ->
                        val isSelected = selectedTipPct == tip
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) AccentPurple else SurfaceContainerHighest)
                                .clickable { selectedTipPct = tip }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$tip%",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }

                // Split Between counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Split Between",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = { if (peopleCount > 1) peopleCount-- },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHighest)
                        ) {
                            Text("-", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }

                        Text(
                            text = "$peopleCount",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = JetBrainsMonoFamily,
                                fontWeight = FontWeight.Bold
                            ),
                            color = AccentPurple
                        )

                        IconButton(
                            onClick = { peopleCount++ },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHighest)
                        ) {
                            Text("+", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }

        // Summary Breakdown Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest.copy(alpha = 0.9f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total with Tip:", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text(
                        "$%.2f".format(totalWithTip),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = JetBrainsMonoFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tip Share Total:", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text(
                        "$%.2f".format(tipTotal),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentCyan
                    )
                }

                // Each Person Pays Highlight Box
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh.copy(alpha = 0.6f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "EACH PERSON PAYS",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$%.2f".format(perPerson),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontFamily = JetBrainsMonoFamily,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = AccentPurple
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Diversity3,
                            contentDescription = null,
                            tint = AccentPurple,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Button(
                    onClick = {
                        val summary = "Check Total: $%.2f (with %d%% tip) across %d guests = $%.2f per person. Split with Collection of Ram."
                            .format(totalWithTip, selectedTipPct, peopleCount, perPerson)
                        onCopyText(summary)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainerHighest,
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Breakdown to Group", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
