package com.pockethome.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pockethome.app.viewmodel.PocketHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: PocketHomeViewModel,
    modifier: Modifier = Modifier
) {
    var amountInput by remember { mutableStateOf("250") }
    var selectedCategory by remember { mutableStateOf("Groceries") }
    var noteInput by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Cash") }
    var selectedDate by remember { mutableStateOf("4 Oct 2026") }
    var expandedDropdown by remember { mutableStateOf(false) }

    val categoriesList = listOf(
        CategoryGridItem("Groceries", Icons.Default.ShoppingCart, Color(0xFF10B981)),
        CategoryGridItem("Bills", Icons.Default.ReceiptLong, Color(0xFF3B82F6)),
        CategoryGridItem("Food", Icons.Default.Restaurant, Color(0xFFF97316)),
        CategoryGridItem("Transport", Icons.Default.DirectionsCar, Color(0xFF14B8A6)),
        CategoryGridItem("Health", Icons.Default.Favorite, Color(0xFFEC4899)),
        CategoryGridItem("Shopping", Icons.Default.ShoppingBag, Color(0xFFD946EF)),
        CategoryGridItem("Education", Icons.Default.School, Color(0xFF06B6D4)),
        CategoryGridItem("Others", Icons.Default.Category, Color(0xFF8B5CF6))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.testTag("add_expense_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1F2937)
                )
            }

            Text(
                text = "Add Expense",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    fontSize = 20.sp
                )
            )

            IconButton(
                onClick = { viewModel.navigateTo("home") },
                modifier = Modifier.testTag("add_expense_close_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(0xFF1F2937)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = "Amount",
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFEEF2F6))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "₹ ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color(0xFF1F2937)
                        )
                        BasicTextField(
                            value = amountInput,
                            onValueChange = { amountInput = it },
                            textStyle = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = Color(0xFF1F2937),
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(180.dp)
                                .testTag("amount_text_field"),
                            singleLine = true
                        )
                    }
                }
            }

            Text(
                text = "Category",
                color = Color(0xFF6B7280),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categoriesList) { item ->
                        val isSelected = selectedCategory == item.name
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) item.color.copy(alpha = 0.12f) else Color.Transparent)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.dp,
                                    color = if (isSelected) item.color else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedCategory = item.name }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) item.color else item.color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name,
                                    tint = if (isSelected) Color.White else item.color,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Column {
                Text(
                    text = "Date",
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Select Date"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4F46E5),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    ),
                    singleLine = true
                )
            }

            Column {
                Text(
                    text = "Note",
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    placeholder = { Text("e.g. Sabzi mandi se samaan", color = Color(0xFF9CA3AF)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_input_field"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4F46E5),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    ),
                    singleLine = true
                )
            }

            Column {
                Text(
                    text = "Payment Method",
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box {
                    OutlinedButton(
                        onClick = { expandedDropdown = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("payment_method_dropdown"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1F2937)),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = paymentMethod, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }
                    }

                    DropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Color.White)
                    ) {
                        listOf("Cash", "Card", "UPI", "Net Banking").forEach { method ->
                            DropdownMenuItem(
                                text = { Text(method, fontWeight = FontWeight.Medium) },
                                onClick = {
                                    paymentMethod = method
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val amt = amountInput.toDoubleOrNull() ?: 0.0
                    if (amt > 0) {
                        viewModel.addExpense(selectedCategory, amt, noteInput, selectedDate, paymentMethod)
                    }
                    viewModel.navigateTo("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("save_expense_button")
                    .navigationBarsPadding(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "Save Expense",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

data class CategoryGridItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)
