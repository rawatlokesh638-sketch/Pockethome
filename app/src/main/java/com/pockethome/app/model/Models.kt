package com.pockethome.app.model

data class ExpenseCategory(
    val name: String,
    val percentage: Int,
    val amount: Double,
    val budget: Double,
    val hexColor: String
)

data class BillItem(
    val id: String,
    val title: String,
    val amount: Double,
    val dueDate: String,
    val dueInDays: Int,
    val isPast: Boolean,
    val iconType: String // "electricity", "internet", "mobile", "gas", "water"
)

data class TransactionItem(
    val id: String,
    val categoryName: String,
    val amount: Double,
    val date: String,
    val note: String,
    val paymentMethod: String
)
