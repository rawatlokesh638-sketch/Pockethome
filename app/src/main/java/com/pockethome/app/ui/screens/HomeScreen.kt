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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pockethome.app.model.ExpenseCategory
import com.pockethome.app.viewmodel.PocketHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PocketHomeViewModel,
    modifier: Modifier = Modifier
) {
    val todaysExpense by viewModel.todaysExpense.collectAsState()
    val timeframe by viewModel.selectedTimeframe.collectAsState()
    val budget by viewModel.monthlyBudget.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val totalExpenses = remember(categories) { categories.sumOf { it.amount } }
    val progress = remember(totalExpenses, budget) {
        if (budget > 0) (totalExpenses / budget).toFloat().coerceIn(0f, 1f) else 0f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "budget_progress"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item { Spacer(modifier = Modifier.height(55.dp)) }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hi Lokesh 👋",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aaj ka kharcha: ₹${todaysExpense.toInt()}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFF6B7280),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                IconButton(
                    onClick = { /* Action */ },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White, CircleShape)
                        .testTag("notification_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF3F51B5)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE6E8F0))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val isMonth = timeframe == "This Month"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isMonth) Color(0xFF4F46E5) else Color.Transparent)
                        .clickable { viewModel.setTimeframe("This Month") }
                        .testTag("tab_month"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "This Month",
                        color = if (isMonth) Color.White else Color(0xFF4B5563),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!isMonth) Color(0xFF4F46E5) else Color.Transparent)
                        .clickable { viewModel.setTimeframe("This Year") }
                        .testTag("tab_year"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "This Year",
                        color = if (!isMonth) Color.White else Color(0xFF4B5563),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
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
                                text = "Total Expenses",
                                color = Color(0xFF6B7280),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹${String.format("%,.0f", totalExpenses)}",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937)
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFEE2E2))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "+12%",
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Monthly Budget",
                            color = Color(0xFF4B5563),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "₹${String.format("%,.0f", budget)}",
                            color = Color(0xFF9CA3AF),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(CircleShape),
                        color = Color(0xFF10B981),
                        trackColor = Color(0xFFE6E8F0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val leftAmount = budget - totalExpenses
                    Text(
                        text = if (leftAmount >= 0) "₹${String.format("%,.0f", leftAmount)} left" else "₹${String.format("%,.0f", -leftAmount)} over budget",
                        color = if (leftAmount >= 0) Color(0xFF1F2937) else Color(0xFFEF4444),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Add Expense",
                    iconColor = Color(0xFF10B981),
                    icon = Icons.Rounded.Add,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.navigateTo("add_expense")
                }

                QuickActionCard(
                    title = "Add Bill",
                    iconColor = Color(0xFFF97316),
                    icon = Icons.Rounded.ReceiptLong,
                    modifier = Modifier.weight(1f)
                ) {
                    viewModel.navigateTo("bills")
                }

                QuickActionCard(
                    title = "Add Income",
                    iconColor = Color(0xFF3B82F6),
                    icon = Icons.Rounded.ArrowUpward,
                    modifier = Modifier.weight(1f)
                ) {
                    // Action
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            Text(
                text = "Expense Overview",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(categories) { category ->
            ExpenseCategoryItem(category = category)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    iconColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .height(115.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = Color(0xFF1F2937),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ExpenseCategoryItem(category: ExpenseCategory) {
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

    val themeColor = Color(android.graphics.Color.parseColor(category.hexColor))

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
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
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${category.percentage}%",
                    color = Color(0xFF9CA3AF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = "₹${String.format("%,.0f", category.amount)}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    fontSize = 15.sp
                )
            }
        }
    }
}
