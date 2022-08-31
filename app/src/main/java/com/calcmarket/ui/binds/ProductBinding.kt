package com.calcmarket.ui.binds

import com.calcmarket.data.local.entities.ProductEntity

data class ProductBinding(
    val id: Int,
    val name: String = "",
    val costItem: Int = 0,
    var amount: Int = 0,
    var total: Int = 0
) {

    fun toEntity(idBuy: Int) = ProductEntity(
        id = 0,
        name = name,
        costItem = costItem,
        amount = amount,
        buyId = idBuy
    )
}