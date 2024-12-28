package com.calcmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.calcmarket.ui.binds.ProductBinding
import com.calcmarket.ui.binds.ProductsBuyBinding

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
    val isFavorite: Boolean = false,

    @ColumnInfo(name = NAME_COLUMN_TYPE )
    val type: String = "",

    @ColumnInfo(name = NAME_COLUMN_MEASURE )
    val measure: String = "",

) {
    fun toBinding() = ProductBinding(
        id = id,
        name = name,
        costItem = costProduct,
        type = type,
        measure = measure,
        isFavorite = isFavorite
    )

    companion object {
        const val NAME_TABLE = "product"
        const val NAME_COLUMN_ID = "${NAME_TABLE}_id"
        const val NAME_COLUMN_NAME = "${NAME_TABLE}_name"
        const val NAME_COLUMN_COST = "${NAME_TABLE}_cost"
        const val NAME_COLUMN_FAVORITE = "${NAME_TABLE}_favorite"
        const val NAME_COLUMN_TYPE = "${NAME_TABLE}_type"
        const val NAME_COLUMN_MEASURE = "${NAME_TABLE}_measure"
    }
}