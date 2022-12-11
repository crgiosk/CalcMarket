package com.calcmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.calcmarket.ui.binds.ProductBinding

@Entity(
    tableName = "product",
    indices = [ Index(value = [ProductEntity.FK_BUY_ID], unique = false)]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PRODUCT_ID)
    val id: Int,

    @ColumnInfo(name = "product_name")
    val name: String = "",

    @ColumnInfo(name = "product_cost")
    val costItem: Int =  0,

    @ColumnInfo(name = "product_total")
    val total: Int =  0,

    @ColumnInfo(name = "product_amount")
    val amount: Int =  0,

    @ColumnInfo(name = FK_BUY_ID)
    val buyId: Int =  0

) {
    fun toBinding() = ProductBinding(
        id = id,
        name = name,
        costItem = costItem,
        amount = amount,
        total = total
    )

    companion object {
        const val FK_BUY_ID = "fk_${BuyEntity.BUY_ID}"
        const val PRODUCT_ID = "product_id"
    }
}