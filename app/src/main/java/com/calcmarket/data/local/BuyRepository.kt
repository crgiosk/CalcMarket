package com.calcmarket.data.local

import com.calcmarket.data.local.daos.BuyDAO
import com.calcmarket.data.local.daos.ProductDAO
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductEntity
import javax.inject.Inject

class BuyRepository @Inject constructor(
    private val buyDAO: BuyDAO,
    private val productDAO: ProductDAO
) {

    fun saveLocalBuy(
        buyEntity: BuyEntity,
        productEntity: List<ProductEntity>
    ) {
        buyDAO.saveBuy(buyEntity)
        productDAO.saveProduct(productEntity)
    }

    fun getFullBuys() = buyDAO.getFullBuys()

    fun getProductByQuery(query: String) = productDAO.getNameProducts(query)
}