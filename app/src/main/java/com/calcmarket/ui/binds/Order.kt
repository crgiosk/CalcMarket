package com.calcmarket.ui.binds

data class Order(
    val id: Int,
    val name: String = "",
    val code: String = "",
    val numberPage: String = "",
    val costItem: String = "",
    val amount: String = "",
    val total: String = ""
)