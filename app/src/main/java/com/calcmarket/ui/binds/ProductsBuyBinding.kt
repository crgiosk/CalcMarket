package com.calcmarket.ui.binds

import com.calcmarket.data.local.entities.ProductsByBuyEntity

//create interface Product? para mejor manejo
data class ProductsBuyBinding(
    var id: Int = 0,
    var productBuyId: Int = 0,
    val name: String = "",
    var costItem: Int = 0,
    var amount: Int = 0,
    var total: Int = 0,
    val isFavorite: Boolean = false,
    val type: String = "",
    val measure: String = ""
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