package com.calcmarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "buy"
)
data class BuyEntity(
    //la primary key, sera la fecha en milis
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = BUY_ID)
    val id: Int,

    @ColumnInfo(name = "buy_name")
    val name: String = "",

    @ColumnInfo(name = "buy_items")
    val countItems: Int =  0,

    @ColumnInfo(name = "buy_total")
    val total: Int =  0
) {
    companion object {
        const val BUY_ID = "buy_id"
    }
}