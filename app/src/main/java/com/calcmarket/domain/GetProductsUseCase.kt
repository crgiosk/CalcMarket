package com.calcmarket.domain

import com.calcmarket.data.network.FirebaseProductsService
import com.calcmarket.ui.binds.ProductBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val firebaseProductsService: FirebaseProductsService,
) {

    operator fun invoke(): Flow<List<ProductBinding>> {
        return firebaseProductsService.subscribeAndGetToProducts().map { collected ->
            collected.map { it.toProductBinding() }
        }
    }

}