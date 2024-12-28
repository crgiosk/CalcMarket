package com.calcmarket.data.local.repository

import com.calcmarket.data.local.daos.BuyDAO
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BuyRepository @Inject constructor(
    private val buyDAO: BuyDAO
) {

    fun saveLocalBuy(buyEntity: BuyEntity) = buyDAO.saveBuy(buyEntity)

    fun saveProductsByBuy(products: List<ProductsByBuyEntity>): Flow<LongArray> {
        return flow {
            emit(buyDAO.saveProducts(products))
        }
    }

    fun updateCostAmountItemBuy(product: ProductsByBuyEntity): Flow<Result<Boolean>> = flow {
        try {
            val rowsAffected = buyDAO.updateCostAmountItemBuy(product)
            if (rowsAffected > 0) {
                emit(Result.success(true)) // La operación fue exitosa
            } else {
                emit(Result.failure(Exception("Error unexpected"))) // Operación sin cambios
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }

    }

    fun deleteItemBuy(product: ProductsByBuyEntity): Flow<Result<Boolean>> = flow {
        try {
            val rowsAffected = buyDAO.deleteItemBuy(product)
            if (rowsAffected > 0) {
                emit(Result.success(true))
            } else {
                emit(Result.failure(Exception("Error unexpected")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }

    }

    fun getAllLocalBuy(): Flow<List<BuyEntity>> = buyDAO.getAllLocalBuy()

    fun getProductsByBuy(idBuy: Int): Flow<List<ProductsByBuyList>> = buyDAO.getProductsByBuy(idBuy)

    suspend fun checkBuyInProgress(): BuyEntity? = buyDAO.checkBuyInProgress()

    suspend fun deleteBuyInProgress(idBuy: Int) = buyDAO.deleteBuyWithProduct(idBuy)

    suspend fun updateItemsAndCostItemsBuy(idBuy: Int, countItems: Int, total: Int): Int =
        buyDAO.updateItemsAndCostItemsBuy(idBuy, countItems, total)
}