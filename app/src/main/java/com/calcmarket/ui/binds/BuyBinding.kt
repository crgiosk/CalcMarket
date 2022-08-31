package com.calcmarket.ui.binds

import androidx.room.ColumnInfo

data class BuyBinding(
    val id: Int,
    val name: String = "",
    val totalBuyValue: Int =  0,
    val items: List<ProductBinding>
)