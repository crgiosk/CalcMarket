package com.calcmarket.data.usecase

import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyList
import com.calcmarket.data.local.repository.BuyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BuyUseCase @Inject constructor(
    private val repository: BuyRepository
) {
    fun saveLocalBuy(buyEntity: BuyEntity) = repository.saveLocalBuy(buyEntity)

    fun saveProductsByBuy(product: List<ProductsByBuyEntity>) = repository.saveProductsByBuy(product)

    suspend fun checkBuyInProgress(): BuyEntity? = repository.checkBuyInProgress()

    suspend fun deleteBuyInProgress(idBuy: Int) = repository.deleteBuyInProgress(idBuy)

    suspend fun updateCostAmountItemBuy(product: ProductsByBuyEntity) = repository.updateCostAmountItemBuy(product)

    suspend fun deleteItemBuy(product: ProductsByBuyEntity) = repository.deleteItemBuy(product)

    fun getFullBuys(): Flow<List<BuyEntity>> = repository.getAllLocalBuy()

    fun getProductsByBuy(idBuy: Int): Flow<List<ProductsByBuyList>> = repository.getProductsByBuy(idBuy)

    suspend fun updateItemsAndCostItemsBuy(idBuy: Int, countItems: Int, total: Int): Int =
        repository.updateItemsAndCostItemsBuy(idBuy, countItems, total)

}