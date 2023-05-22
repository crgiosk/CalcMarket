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

    fun getAllLocalBuy(): Flow<List<BuyEntity>> = buyDAO.getAllLocalBuy()

    fun getProductsByBuy(idBuy: Int): Flow<List<ProductsByBuyList>> = buyDAO.getProductsByBuy(idBuy)
}