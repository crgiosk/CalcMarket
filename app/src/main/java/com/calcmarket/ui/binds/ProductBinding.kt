package com.calcmarket.ui.binds

import com.calcmarket.data.local.entities.ProductEntity
import com.calcmarket.data.local.entities.ProductsByBuyEntity

data class ProductBinding(
    val id: Int = 0,
    val name: String = "",
    var costItem: Int = 0,
    var amount: Int = 0,
    var total: Int = 0
) {
    fun toEntity(idBuy: Int): ProductsByBuyEntity {
        return ProductsByBuyEntity(
            buyId = idBuy,
            productId = id,
            total = total,
            amount = amount,
        )
    }
}