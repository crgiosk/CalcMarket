package com.calcmarket.domain

import com.calcmarket.data.network.FirebaseProductsService
import com.calcmarket.data.network.dto.ProductsFRBDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveFirebaseProductsUseCase @Inject constructor(
    private val firebaseProductsService: FirebaseProductsService,
) {

    operator fun invoke(product: ProductsFRBDTO): Flow<Result<Unit>> {
        return firebaseProductsService.saveNewProductToFirebase(product)
    }

}