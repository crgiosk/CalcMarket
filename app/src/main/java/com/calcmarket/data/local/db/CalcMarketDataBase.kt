package com.calcmarket.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.calcmarket.data.local.daos.BuyDAO
import com.calcmarket.data.local.daos.ProductDAO
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyEntity
import com.calcmarket.data.local.entities.ProductEntity

@Database(
    entities = [
        BuyEntity::class,
        ProductEntity::class,
        ProductsByBuyEntity::class
    ],
    version = 1
)
abstract class CalcMarketDataBase : RoomDatabase() {
    abstract fun getBuyDAO(): BuyDAO

    abstract fun getProductDAO(): ProductDAO

}