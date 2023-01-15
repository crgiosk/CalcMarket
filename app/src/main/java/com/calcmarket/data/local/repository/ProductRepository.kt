package com.calcmarket.data.local.repository

import com.calcmarket.data.local.daos.ProductDAO
import com.calcmarket.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDAO: ProductDAO
) {

    fun getProductByQuery(query: String) = productDAO.getProductsByName(query)

    fun saveProduct(productEntity: ProductEntity) = productDAO.saveProduct(productEntity)
}