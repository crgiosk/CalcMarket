package com.calcmarket.ui.binds

import com.calcmarket.data.local.entities.ProductsByBuyEntity

data class ProductsBuyBinding(
    var id: Int = 0,
    var productBuyId: Int = 0,
    val name: String = "",
    var costItem: Int = 0,
    var amount: Int = 0,
    var total: Int = 0,
    val isFavorite: Boolean = false
) {
    fun toEntity(idBuy: Int): ProductsByBuyEntity {
        return ProductsByBuyEntity(
            id = productBuyId,
            buyId = idBuy,
            productId = id,
            total = total,
            amount = amount,
        )
    }
}