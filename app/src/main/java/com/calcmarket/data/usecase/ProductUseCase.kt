package com.calcmarket.data.usecase

import com.calcmarket.data.local.repository.BuyRepository
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductEntity
import com.calcmarket.data.local.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {

    fun getProductByQuery(query: String) = repository.getProductByQuery(query)

    fun getAllProducts() = repository.getAllProducts()

    suspend fun getProductByName(name: String) = repository.getProductByName(name)

    fun saveProduct(productEntity: ProductEntity) = repository.saveProduct(productEntity)

    fun saveProducts(productsEntity: List<ProductEntity>): Flow<LongArray> = repository.saveProducts(productsEntity)

}