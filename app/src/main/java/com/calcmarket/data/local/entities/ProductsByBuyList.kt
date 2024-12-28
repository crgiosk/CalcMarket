package com.calcmarket.data.local.entities

import androidx.room.ColumnInfo
import com.calcmarket.ui.binds.ProductsBuyBinding

data class ProductsByBuyList(
    @ColumnInfo(name = ProductEntity.NAME_COLUMN_ID)
    val productId: Int,

    @ColumnInfo(name = NAME_COLUMN_PRODUCT_BUY_ID)
    val productBuyId: Int,

    @ColumnInfo(name = ProductEntity.NAME_COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = ProductEntity.NAME_COLUMN_TYPE)
    val type: String,

    @ColumnInfo(name = ProductEntity.NAME_COLUMN_MEASURE)
    val measure: String,

    @ColumnInfo(name = ProductEntity.NAME_COLUMN_COST)
    val costProduct: Int,

    @ColumnInfo(name = ProductEntity.NAME_COLUMN_FAVORITE)
    val isFavorite: Boolean,

    @ColumnInfo(name = ProductsByBuyEntity.NAME_COLUM_AMOUNT)
    val amount: Int,

    @ColumnInfo(name = ProductsByBuyEntity.NAME_COLUM_TOTAL)
    val total: Int


) {

    fun toProductsBuyBinding(): ProductsBuyBinding {
        return ProductsBuyBinding(
            id = productBuyId,
            productId = productId,
            name = name,
            costItem = costProduct,
            amount = amount,
            total = total,
            isFavorite = isFavorite,
            type = type,
            measure = measure
        )
    }

    companion object {
        const val NAME_COLUMN_PRODUCT_BUY_ID = "productBuyId"
    }
}
