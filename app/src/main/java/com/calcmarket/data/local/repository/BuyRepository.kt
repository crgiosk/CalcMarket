package com.calcmarket.data.local.repository

import com.calcmarket.data.local.daos.BuyDAO
import com.calcmarket.data.local.daos.ProductDAO
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.FullBuyEntity
import com.calcmarket.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BuyRepository @Inject constructor(
    private val buyDAO: BuyDAO,
    private val productDAO: ProductDAO
) {

    fun saveLocalBuy(
        buyEntity: BuyEntity,
        productEntity: List<Int>
    ) {
        buyDAO.saveBuy(buyEntity)
        //TODO!! buyDAO.saveProductByBuy()
        //TODO!! save buyProducts with itemsIdList
    }

    fun getFullBuys(): Flow<List<FullBuyEntity>> = buyDAO.getFullBuys()

    //fun getProductByQuery(query: String): Flow<List<String>> = productDAO.getNameProducts(query)
}