package com.calcmarket.ui.binds

import com.calcmarket.data.local.entities.ProductsByBuyEntity

//create interface Product? para mejor manejo
data class ProductsBuyBinding(
    var id: Int = 0,
    var productId: Int = 0,
    val name: String = "",
    var costItem: Int = 0,
    var amount: Int = 0,
    var total: Int = 0,
    val isFavorite: Boolean = false,
    var type: String = "",
    var measure: String = ""
) {
    fun toEntity(idBuy: Int): ProductsByBuyEntity {
        return ProductsByBuyEntity(
            id = id,
            buyId = idBuy,
            productId = productId,
            total = total,
            amount = amount,
        )
    }

    fun toProductBinding(): ProductBinding {
        return ProductBinding(
            id = productId,
            name = name,
            costItem = costItem,
            type = type,
            measure = measure,
        )
    }
}