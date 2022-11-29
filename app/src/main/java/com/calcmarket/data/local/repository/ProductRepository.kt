package com.calcmarket.data.local.repository

import com.calcmarket.data.local.daos.BuyDAO
import com.calcmarket.data.local.daos.ProductDAO
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDAO: ProductDAO
) {

    fun getProductByQuery(query: String): Flow<List<String>> = productDAO.getNameProducts(query)

    fun saveProduct(productEntity: ProductEntity) = productDAO.saveProduct(productEntity)
}