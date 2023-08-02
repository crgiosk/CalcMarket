package com.calcmarket.data.usecase

import com.calcmarket.data.local.repository.BuyRepository
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductEntity
import com.calcmarket.data.local.repository.ProductRepository
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {

    fun getProductByQuery(query: String) = repository.getProductByQuery(query)

    suspend fun getProductByName(name: String) = repository.getProductByName(name)

    fun saveProduct(productEntity: ProductEntity) = repository.saveProduct(productEntity)

}