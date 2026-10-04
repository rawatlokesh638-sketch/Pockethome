package com.pockethome.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pockethome.app.model.ExpenseCategory
import com.pockethome.app.viewmodel.PocketHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: PocketHomeViewModel,
    modifier: Modifier = Modifier
) {
    val budget by viewModel.monthlyBudget.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var budgetInput by remember { mutableStateOf(budget.toInt().toString()) }

    val totalExpenses = remember(categories) { categories.sumOf { it.amount } }
    val percentUsed = remember(totalExpenses, budget) {
        if (budget > 0) ((totalExpenses / budget) * 100).toInt() else 0
    }
    val leftAmount = budget - totalExpenses

    val monthlyProgress = remember(totalExpenses, budget) {
        if (budget > 0) (totalExpenses / budget).toFloat().coerceIn(0f, 1f) else 0f
    }
    val animatedMonthlyProgress by animateFloatAsState(
        targetValue = monthlyProgress,
        animationSpec = tween(durationMillis = 800),
        label = "monthly_budget_progress"
    )

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Monthly Budget", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = budgetInput,
                    onValueChange = { budgetInput = it },
                    label = { Text("Budget Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("budget_input_field"),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newBudget = budgetInput.toDoubleOrNull() ?: budget
                        viewModel.updateMonthlyBudget(newBudget)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                    modifier = Modifier.testTag("confirm_budget_button")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = Color(0xFF6B7280))
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo("home") },
                modifier = Modifier.testTag("budget_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1F2937)
                )
            }
            Spacer(modifier = Modifier.weight(0.3f))
            Text(
                text = "Budget",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    fontSize = 20.sp
                )
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Previous */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous",
                            tint = Color(0xFF4F46E5)
                        )
                    }

                    Text(
                        text = "October 2026",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            fontSize = 17.sp
                        )
                    )

                    IconButton(onClick = { /* Next */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next",
                            tint = Color(0xFF4F46E5)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Monthly Budget",
                                    color = Color(0xFF6B7280),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "₹${String.format("%,.0f", budget)}",
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                )
                            }

                            Button(
                                onClick = {
                                    budgetInput = budget.toInt().toString()
                                    showEditDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEEF2F6),
                                    contentColor = Color(0xFF4F46E5)
                                ),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("edit_budget_button")
                            ) {
                                Text("Edit", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        LinearProgressIndicator(
                            progress = { animatedMonthlyProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(CircleShape),
                            color = Color(0xFF10B981),
                            trackColor = Color(0xFFE6E8F0)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "₹${String.format("%,.0f", leftAmount)} left",
                                color = if (leftAmount >= 0) Color(0xFF4B5563) else Color(0xFFEF4444),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Text(
                                text = "$percentUsed% used",
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                Text(
                    text = "Category Budgets",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(categories) { category ->
                CategoryBudgetRow(category = category)
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun CategoryBudgetRow(category: ExpenseCategory) {
    val themeColor = Color(android.graphics.Color.parseColor(category.hexColor))
    val categoryIcon = when (category.name) {
        "Groceries" -> Icons.Default.ShoppingCart
        "Bills & Utilities" -> Icons.Default.ReceiptLong
        "Food & Dining" -> Icons.Default.Restaurant
        "Transport" -> Icons.Default.DirectionsCar
        "Health & Medical" -> Icons.Default.Favorite
        "Shopping" -> Icons.Default.ShoppingBag
        "Education" -> Icons.Default.School
        else -> Icons.Default.Category
    }

    val usedRatio = remember(category) {
        if (category.budget > 0) (category.amount / category.budget).toFloat().coerceIn(0f, 1f) else 0f
    }
    val animatedUsedProgress by animateFloatAsState(
        targetValue = usedRatio,
        animationSpec = tween(durationMillis = 800),
        label = "category_used_progress"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(themeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = category.name,
                            tint = themeColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = category.name,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "₹${category.amount.toInt()} / ₹${category.budget.toInt()}",
                            color = Color(0xFF6B7280),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    text = "${((category.amount / category.budget) * 100).toInt()}%",
                    color = Color(0xFF1F2937),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { animatedUsedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = themeColor,
                trackColor = Color(0xFFE6E8F0)
            )
        }
    }
}
