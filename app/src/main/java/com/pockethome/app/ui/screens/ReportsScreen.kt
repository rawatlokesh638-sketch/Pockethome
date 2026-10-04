package com.pockethome.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pockethome.app.model.ExpenseCategory
import com.pockethome.app.viewmodel.PocketHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: PocketHomeViewModel,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()
    val totalExpenses = remember(categories) { categories.sumOf { it.amount } }

    var selectedTab by remember { mutableStateOf("Monthly") }

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
                modifier = Modifier.testTag("reports_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1F2937)
                )
            }
            Spacer(modifier = Modifier.weight(0.3f))
            Text(
                text = "Reports",
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
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE6E8F0))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Monthly", "Yearly", "Custom").forEach { tab ->
                        val isSel = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) Color(0xFF4F46E5) else Color.Transparent)
                                .clickable { selectedTab = tab }
                                .testTag("reports_tab_$tab"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (isSel) Color.White else Color(0xFF4B5563),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
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
                            fontSize = 16.sp
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
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                                        fontSize = 26.sp,
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
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        DonutChart(
                            categories = categories,
                            totalAmount = totalExpenses,
                            modifier = Modifier
                                .size(220.dp)
                                .testTag("reports_donut_chart")
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            items(categories) { category ->
                ReportCategoryRow(category = category)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun DonutChart(
    categories: List<ExpenseCategory>,
    totalAmount: Double,
    modifier: Modifier = Modifier
) {
    val animateProgress = remember { Animatable(0f) }

    LaunchedEffect(categories) {
        animateProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val strokeWidth = 26.dp.toPx()
            var startAngle = -90f

            categories.forEach { category ->
                val sweepAngle = if (totalAmount > 0) {
                    (category.amount / totalAmount).toFloat() * 360f
                } else {
                    0f
                }

                val themeColor = Color(android.graphics.Color.parseColor(category.hexColor))

                drawArc(
                    color = themeColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle * animateProgress.value,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                startAngle += sweepAngle
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "₹${String.format("%,.0f", totalAmount)}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Total",
                fontSize = 13.sp,
                color = Color(0xFF9CA3AF),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ReportCategoryRow(category: ExpenseCategory) {
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
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(themeColor)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = category.name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    fontSize = 15.sp
                )
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
