package com.pockethome.app.viewmodel

import androidx.lifecycle.ViewModel
import com.pockethome.app.model.BillItem
import com.pockethome.app.model.ExpenseCategory
import com.pockethome.app.model.TransactionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class PocketHomeViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow("home")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf("home")

    private val _selectedTimeframe = MutableStateFlow("This Month")
    val selectedTimeframe: StateFlow<String> = _selectedTimeframe.asStateFlow()

    private val _monthlyBudget = MutableStateFlow(25000.0)
    val monthlyBudget: StateFlow<Double> = _monthlyBudget.asStateFlow()

    private val _automaticReminders = MutableStateFlow(true)
    val automaticReminders: StateFlow<Boolean> = _automaticReminders.asStateFlow()

    private val _categories = MutableStateFlow(
        listOf(
            ExpenseCategory("Groceries", 34, 6400.0, 8000.0, "#FBBF24"),
            ExpenseCategory("Bills & Utilities", 18, 3350.0, 5000.0, "#3B82F6"),
            ExpenseCategory("Food & Dining", 12, 2250.0, 3000.0, "#F97316"),
            ExpenseCategory("Transport", 10, 1850.0, 2500.0, "#14B8A6"),
            ExpenseCategory("Health & Medical", 8, 1500.0, 2000.0, "#EC4899"),
            ExpenseCategory("Shopping", 6, 1100.0, 2000.0, "#D946EF"),
            ExpenseCategory("Education", 6, 1100.0, 1500.0, "#06B6D4"),
            ExpenseCategory("Others", 6, 1100.0, 2000.0, "#8B5CF6")
        )
    )
    val categories: StateFlow<List<ExpenseCategory>> = _categories.asStateFlow()

    private val _upcomingBills = MutableStateFlow(
        listOf(
            BillItem(UUID.randomUUID().toString(), "Electricity Bill", 1200.0, "10 Oct 2026", 6, false, "electricity"),
            BillItem(UUID.randomUUID().toString(), "Internet Bill", 799.0, "12 Oct 2026", 8, false, "internet"),
            BillItem(UUID.randomUUID().toString(), "Mobile Recharge", 299.0, "15 Oct 2026", 11, false, "mobile"),
            BillItem(UUID.randomUUID().toString(), "Gas Cylinder", 1100.0, "20 Oct 2026", 16, false, "gas"),
            BillItem(UUID.randomUUID().toString(), "Water Bill", 450.0, "25 Oct 2026", 21, false, "water")
        )
    )
    val upcomingBills: StateFlow<List<BillItem>> = _upcomingBills.asStateFlow()

    private val _pastBills = MutableStateFlow(
        listOf(
            BillItem(UUID.randomUUID().toString(), "Rent Payment", 12000.0, "01 Oct 2026", 0, true, "bills"),
            BillItem(UUID.randomUUID().toString(), "Gym Subscription", 1000.0, "28 Sep 2026", 0, true, "health"),
            BillItem(UUID.randomUUID().toString(), "Milk Supplier", 1500.0, "25 Sep 2026", 0, true, "groceries")
        )
    )
    val pastBills: StateFlow<List<BillItem>> = _pastBills.asStateFlow()

    private val _todaysExpense = MutableStateFlow(320.0)
    val todaysExpense: StateFlow<Double> = _todaysExpense.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionItem>>(emptyList())
    val transactions: StateFlow<List<TransactionItem>> = _transactions.asStateFlow()

    fun navigateTo(screen: String) {
        if (_currentScreen.value != screen) {
            if (screen == "home") {
                screenStack.clear()
                screenStack.add("home")
            } else {
                screenStack.add(screen)
            }
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = screenStack.last()
            return true
        }
        return false
    }

    fun setTimeframe(timeframe: String) {
        _selectedTimeframe.value = timeframe
    }

    fun updateMonthlyBudget(newBudget: Double) {
        _monthlyBudget.value = newBudget
    }

    fun toggleAutomaticReminders() {
        _automaticReminders.value = !_automaticReminders.value
    }

    fun addExpense(categoryName: String, amount: Double, note: String, date: String, paymentMethod: String) {
        val newTx = TransactionItem(
            id = UUID.randomUUID().toString(),
            categoryName = categoryName,
            amount = amount,
            date = date,
            note = note,
            paymentMethod = paymentMethod
        )
        _transactions.value = listOf(newTx) + _transactions.value

        _todaysExpense.value += amount

        val updatedList = _categories.value.map { category ->
            if (category.name.equals(categoryName, ignoreCase = true) || 
                (category.name.startsWith("Bills") && categoryName.equals("Bills", ignoreCase = true))) {
                val newAmount = category.amount + amount
                category.copy(amount = newAmount)
            } else {
                category
            }
        }

        val totalAmount = updatedList.sumOf { it.amount }
        val finalCategories = updatedList.map { category ->
            val percentage = if (totalAmount > 0) ((category.amount / totalAmount) * 100).toInt() else 0
            category.copy(percentage = percentage)
        }

        _categories.value = finalCategories
    }

    fun addBill(title: String, amount: Double, dueDate: String, dueInDays: Int, iconType: String) {
        val newBill = BillItem(
            id = UUID.randomUUID().toString(),
            title = title,
            amount = amount,
            dueDate = dueDate,
            dueInDays = dueInDays,
            isPast = false,
            iconType = iconType
        )
        _upcomingBills.value = listOf(newBill) + _upcomingBills.value
    }
}
