package com.pockethome.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.pockethome.app.ui.screens.*
import com.pockethome.app.ui.theme.MyApplicationTheme
import com.pockethome.app.viewmodel.PocketHomeViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PocketHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer(viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: PocketHomeViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    if (currentScreen != "home") {
        BackHandler {
            viewModel.navigateBack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentScreen != "add_expense") {
                AnimatedBottomBar(
                    currentScreen = currentScreen,
                    onTabSelected = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F8FB))
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    if (targetState == "add_expense" || initialState == "add_expense") {
                        (slideInVertically(
                            animationSpec = spring(stiffness = 300f),
                            initialOffsetY = { it }
                        ) + fadeIn()) togetherWith
                        (slideOutVertically(
                            animationSpec = spring(stiffness = 300f),
                            targetOffsetY = { it }
                        ) + fadeOut())
                    } else {
                        (slideInHorizontally(
                            animationSpec = spring(stiffness = 250f),
                            initialOffsetX = { if (getTabIndex(targetState) > getTabIndex(initialState)) it else -it }
                        ) + fadeIn()) togetherWith
                        (slideOutHorizontally(
                            animationSpec = spring(stiffness = 250f),
                            targetOffsetX = { if (getTabIndex(targetState) > getTabIndex(initialState)) -it else it }
                        ) + fadeOut())
                    }
                },
                label = "screen_transition"
            ) { targetScreen ->
                when (targetScreen) {
                    "home" -> HomeScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                    "transactions" -> ReportsScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                    "budget" -> BudgetScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                    "bills" -> BillsScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                    "more" -> ReportsScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                    "add_expense" -> AddExpenseScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

fun getTabIndex(screen: String): Int {
    return when (screen) {
        "home" -> 0
        "transactions" -> 1
        "budget" -> 2
        "bills" -> 3
        "more" -> 4
        else -> 0
    }
}

@Composable
fun AnimatedBottomBar(
    currentScreen: String,
    onTabSelected: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home, Icons.Outlined.Home),
        BottomNavItem("Transactions", "transactions", Icons.Default.ReceiptLong, Icons.Outlined.ReceiptLong),
        BottomNavItem("Budget", "budget", Icons.Default.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
        BottomNavItem("Bills", "bills", Icons.Default.Receipt, Icons.Outlined.Receipt),
        BottomNavItem("More", "more", Icons.Default.MoreHoriz, Icons.Outlined.MoreHoriz)
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.screen
                val activeIcon = item.filledIcon
                val inactiveIcon = item.outlinedIcon

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(item.screen) }
                        )
                        .testTag("nav_tab_${item.screen}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val animIconTint = if (isSelected) Color(0xFF4F46E5) else Color(0xFF9CA3AF)

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) activeIcon else inactiveIcon,
                            contentDescription = item.title,
                            tint = animIconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        color = animIconTint,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    val title: String,
    val screen: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
)
