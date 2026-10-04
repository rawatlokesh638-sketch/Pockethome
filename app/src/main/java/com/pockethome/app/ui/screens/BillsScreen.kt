package com.pockethome.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import com.pockethome.app.model.BillItem
import com.pockethome.app.viewmodel.PocketHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillsScreen(
    viewModel: PocketHomeViewModel,
    modifier: Modifier = Modifier
) {
    val upcomingBills by viewModel.upcomingBills.collectAsState()
    val pastBills by viewModel.pastBills.collectAsState()
    val remindersEnabled by viewModel.automaticReminders.collectAsState()

    var selectedTab by remember { mutableStateOf("Upcoming") }
    var showAddBillDialog by remember { mutableStateOf(false) }

    var billTitle by remember { mutableStateOf("") }
    var billAmount by remember { mutableStateOf("") }
    var billDueDate by remember { mutableStateOf("15 Oct 2026") }
    var billDueInDays by remember { mutableStateOf("10") }
    var billIconType by remember { mutableStateOf("electricity") }

    if (showAddBillDialog) {
        AlertDialog(
            onDismissRequest = { showAddBillDialog = false },
            title = { Text("Add New Bill", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = billTitle,
                        onValueChange = { billTitle = it },
                        label = { Text("Bill Title") },
                        modifier = Modifier.fillMaxWidth().testTag("bill_title_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = billAmount,
                        onValueChange = { billAmount = it },
                        label = { Text("Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("bill_amount_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = billDueDate,
                        onValueChange = { billDueDate = it },
                        label = { Text("Due Date") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text("Select Icon Type", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("electricity", "internet", "mobile", "gas", "water").forEach { iconName ->
                            val isSel = billIconType == iconName
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isSel) Color(0xFF4F46E5) else Color(0xFFE5E7EB))
                                    .clickable { billIconType = iconName },
                                contentAlignment = Alignment.Center
                            ) {
                                val ic = when (iconName) {
                                    "electricity" -> Icons.Default.ElectricBolt
                                    "internet" -> Icons.Default.Wifi
                                    "mobile" -> Icons.Default.Smartphone
                                    "gas" -> Icons.Default.LocalGasStation
                                    else -> Icons.Default.WaterDrop
                                }
                                Icon(
                                    imageVector = ic,
                                    contentDescription = iconName,
                                    tint = if (isSel) Color.White else Color(0xFF4B5563),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = billAmount.toDoubleOrNull() ?: 0.0
                        val dueDays = billDueInDays.toIntOrNull() ?: 10
                        if (billTitle.isNotBlank()) {
                            viewModel.addBill(billTitle, amt, billDueDate, dueDays, billIconType)
                            showAddBillDialog = false
                            billTitle = ""
                            billAmount = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                    modifier = Modifier.testTag("confirm_bill_button")
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBillDialog = false }) {
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
                modifier = Modifier.testTag("bills_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1F2937)
                )
            }
            Spacer(modifier = Modifier.weight(0.3f))
            Text(
                text = "Bills & Reminders",
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
                    val isUpcoming = selectedTab == "Upcoming"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isUpcoming) Color(0xFF4F46E5) else Color.Transparent)
                            .clickable { selectedTab = "Upcoming" }
                            .testTag("tab_upcoming"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Upcoming",
                            color = if (isUpcoming) Color.White else Color(0xFF4B5563),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!isUpcoming) Color(0xFF4F46E5) else Color.Transparent)
                            .clickable { selectedTab = "Past" }
                            .testTag("tab_past"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Past",
                            color = if (!isUpcoming) Color.White else Color(0xFF4B5563),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                    },
                    label = "bills_list_transition"
                ) { tab ->
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        val listToShow = if (tab == "Upcoming") upcomingBills else pastBills
                        listToShow.forEach { bill ->
                            BillRowItem(bill = bill)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Button(
                    onClick = { showAddBillDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("add_new_bill_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add New Bill",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
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
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = "Reminder Icon",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Automatic Reminders",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937),
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Get notified before due date. Never miss a bill again!",
                                    color = Color(0xFF6B7280),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Switch(
                            checked = remindersEnabled,
                            onCheckedChange = { viewModel.toggleAutomaticReminders() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF4F46E5),
                                uncheckedThumbColor = Color(0xFFE5E7EB),
                                uncheckedTrackColor = Color(0xFF9CA3AF).copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.testTag("reminder_switch")
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BillRowItem(bill: BillItem) {
    val themeColor = when (bill.iconType) {
        "electricity" -> Color(0xFFFBBF24)
        "internet" -> Color(0xFF8B5CF6)
        "mobile" -> Color(0xFF3B82F6)
        "gas" -> Color(0xFFEF4444)
        "water" -> Color(0xFF06B6D4)
        else -> Color(0xFF10B981)
    }

    val iconVector = when (bill.iconType) {
        "electricity" -> Icons.Default.ElectricBolt
        "internet" -> Icons.Default.Wifi
        "mobile" -> Icons.Default.Smartphone
        "gas" -> Icons.Default.LocalGasStation
        "water" -> Icons.Default.WaterDrop
        else -> Icons.Default.ReceiptLong
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
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
                        imageVector = iconVector,
                        contentDescription = bill.title,
                        tint = themeColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = bill.title,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = bill.dueDate,
                        color = Color(0xFF6B7280),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${String.format("%,.0f", bill.amount)}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (bill.isPast) "Paid" else "Due in ${bill.dueInDays} days",
                    color = if (bill.isPast) Color(0xFF10B981) else Color(0xFFEF4444),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
