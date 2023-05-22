package com.calcmarket.data.local.entities

import androidx.room.ColumnInfo
import com.calcmarket.ui.binds.ProductBinding

data class ProductsByBuyList(
    @ColumnInfo(name = ProductEntity.NAME_COLUMN_ID)
    val productId: Int,
    @ColumnInfo(name = ProductEntity.NAME_COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = ProductEntity.NAME_COLUMN_COST)
    val costProduct: Int,
    @ColumnInfo(name = ProductEntity.NAME_COLUMN_FAVORITE)
    val isFavorite: Boolean,
    @ColumnInfo(name = ProductsByBuyEntity.NAME_COLUM_AMOUNT)
    val amount: Int,
    @ColumnInfo(name = ProductsByBuyEntity.NAME_COLUM_TOTAL)
    val total: Int

) {

    fun toBinding(): ProductBinding {
        return ProductBinding(
            id = productId,
            name = name,
            costItem = costProduct,
            amount = amount,
            total = total,
            isFavorite = isFavorite
        )
    }
}
