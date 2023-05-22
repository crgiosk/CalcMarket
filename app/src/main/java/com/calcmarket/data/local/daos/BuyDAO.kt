package com.calcmarket.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyList
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveBuy(buyEntity: BuyEntity)

    @Query("SELECT * FROM buy")
    fun getAllLocalBuy(): Flow<List<BuyEntity>>

    @Insert
    fun saveProducts(products: List<ProductsByBuyEntity>)

    @Query("SELECT prod.*, p.products_by_buy_amount, p.products_by_buy_total " +
            "FROM products_by_buy p " +
            "INNER join product prod ON (p.fk_product_id = prod.product_id) " +
            "WHERE p.fk_buy_id = :idBuy"
    )
    fun getProductsByBuy(idBuy: Int): Flow<List<ProductsByBuyList>>

}