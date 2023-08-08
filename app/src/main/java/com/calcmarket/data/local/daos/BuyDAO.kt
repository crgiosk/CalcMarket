package com.calcmarket.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import androidx.room.Update
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

    @Query("SELECT prod.*, p.products_by_buy_amount, p.products_by_buy_total, " +
            "p.products_by_buy_id as productBuyId " +
            "FROM products_by_buy p " +
            "INNER join product prod ON (p.fk_product_id = prod.product_id) " +
            "WHERE p.fk_buy_id = :idBuy"
    )
    fun getProductsByBuy(idBuy: Int): Flow<List<ProductsByBuyList>>

    @Query("SELECT * FROM buy WHERE buy_in_progress = 1 ORDER BY buy_id DESC LIMIT 1 ")
    suspend fun checkBuyInProgress(): BuyEntity?

    @Query("DELETE from buy WHERE buy_id=:idBuy AND buy_in_progress = 1")
    suspend fun deleteBuyInProgress(idBuy: Int): Int

    @Query("DELETE from products_by_buy WHERE fk_buy_id=:idBuy")
    suspend fun deleteProductByBuy(idBuy: Int): Int

    @Transaction
    suspend fun deleteBuyWithProduct(idBuy: Int) {
        deleteProductByBuy(idBuy)
        deleteBuyInProgress(idBuy)
    }

    @Query("UPDATE buy SET buy_items = :countItems, buy_total = :total WHERE buy_id = :idBuy")
    suspend fun updateItemsAndCostItemsBuy(idBuy: Int, countItems: Int, total: Int): Int

    @Update
    suspend fun updateCostAmountItemBuy(product: ProductsByBuyEntity)

    @Delete
    suspend fun deleteItemBuy(product: ProductsByBuyEntity)
}