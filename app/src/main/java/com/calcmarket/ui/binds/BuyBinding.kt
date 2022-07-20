package com.calcmarket.ui.binds

data class BuyBinding(
    val id: Int,
    val name: String = "",
    val costItem: Int =  0,
    var amount: Int =  0,
    var total: Int =  0
)