package com.calcmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = ProductsByBuyEntity.NAME_TABLE,
    indices = [
        Index(ProductsByBuyEntity.NAME_COLUM_ID),
        Index(ProductsByBuyEntity.FOREIGN_KEY_PRODUCT_COLUM),
        Index(ProductsByBuyEntity.FOREIGN_KEY_BUY_COLUM)
    ],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = [ProductEntity.NAME_COLUMN_ID],
            childColumns = [ProductsByBuyEntity.FOREIGN_KEY_PRODUCT_COLUM],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BuyEntity::class,
            parentColumns = [BuyEntity.BUY_ID],
            childColumns = [ProductsByBuyEntity.FOREIGN_KEY_BUY_COLUM],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ProductsByBuyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NAME_COLUM_ID)
    val id: Int = 0,

    @ColumnInfo(name = FOREIGN_KEY_BUY_COLUM)
    val buyId: Int = 0,

    @ColumnInfo(name = FOREIGN_KEY_PRODUCT_COLUM)
    val productId: Int = 0,

    @ColumnInfo(name = NAME_COLUM_TOTAL)
    val total: Int = 0,

    @ColumnInfo(name = NAME_COLUM_AMOUNT)
    val amount: Int = 0

) {
    companion object {
        const val NAME_TABLE = "products_by_buy"
        const val NAME_COLUM_ID = "${NAME_TABLE}_id"
        const val NAME_COLUM_TOTAL = "${NAME_TABLE}_total"
        const val NAME_COLUM_AMOUNT = "${NAME_TABLE}_amount"
        const val FOREIGN_KEY_BUY_COLUM = "fk_${BuyEntity.BUY_ID}"
        const val FOREIGN_KEY_PRODUCT_COLUM = "fk_${ProductEntity.NAME_COLUMN_ID}"
    }
}