package com.calcmarket.data.local.repository

import com.calcmarket.data.local.daos.BuyDAO
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BuyRepository @Inject constructor(
    private val buyDAO: BuyDAO
) {

    fun saveLocalBuy(buyEntity: BuyEntity) = buyDAO.saveBuy(buyEntity)

    fun saveProductsByBuy(products: List<ProductsByBuyEntity>) = buyDAO.saveProducts(products)

    suspend fun updateCostAmountItemBuy(product: ProductsByBuyEntity) = buyDAO.updateCostAmountItemBuy(product)

    suspend fun deleteItemBuy(product: ProductsByBuyEntity) = buyDAO.deleteItemBuy(product)

    fun getAllLocalBuy(): Flow<List<BuyEntity>> = buyDAO.getAllLocalBuy()

    fun getProductsByBuy(idBuy: Int): Flow<List<ProductsByBuyList>> = buyDAO.getProductsByBuy(idBuy)

    suspend fun checkBuyInProgress(): BuyEntity? = buyDAO.checkBuyInProgress()

    suspend fun deleteBuyInProgress(idBuy: Int) = buyDAO.deleteBuyWithProduct(idBuy)

    suspend fun updateItemsAndCostItemsBuy(idBuy: Int, countItems: Int, total: Int): Int =
        buyDAO.updateItemsAndCostItemsBuy(idBuy, countItems, total)
}