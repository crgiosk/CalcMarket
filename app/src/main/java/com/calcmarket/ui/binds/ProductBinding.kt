package com.calcmarket.ui.binds

import com.calcmarket.data.local.entities.ProductEntity

data class ProductBinding(
    val id: Int = 0,
    val name: String = "",
    val costItem: Int = 0,
    var amount: Int = 0,
    var total: Int = 0
)