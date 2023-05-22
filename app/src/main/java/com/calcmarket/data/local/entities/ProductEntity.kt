package com.calcmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.calcmarket.ui.binds.ProductBinding

@Entity(tableName = ProductEntity.NAME_TABLE)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NAME_COLUMN_ID)
    val id: Int = 0,

    @ColumnInfo(name = NAME_COLUMN_NAME )
    val name: String = "",

    @ColumnInfo(name = NAME_COLUMN_COST )
    val costProduct: Int = 0,

    @ColumnInfo(name = NAME_COLUMN_FAVORITE )
    val isFavorite: Boolean = false

) {
    fun toBinding() = ProductBinding(
        id = id,
        name = name,
        costItem = costProduct
    )

    companion object {
        const val NAME_TABLE = "product"
        const val NAME_COLUMN_ID = "${NAME_TABLE}_id"
        const val NAME_COLUMN_NAME = "${NAME_TABLE}_name"
        const val NAME_COLUMN_COST = "${NAME_TABLE}_cost"
        const val NAME_COLUMN_FAVORITE = "${NAME_TABLE}_favorite"
    }
}