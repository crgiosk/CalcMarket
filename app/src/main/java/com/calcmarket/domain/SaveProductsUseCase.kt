package com.calcmarket.domain

import com.calcmarket.data.network.FirebaseProductsService
import com.calcmarket.data.network.dto.ProductsFRBDTO
import com.calcmarket.ui.binds.ProductBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SaveProductsUseCase @Inject constructor(
    private val firebaseProductsService: FirebaseProductsService,
) {

    operator fun invoke(product: ProductsFRBDTO) {
        firebaseProductsService.saveNewProductToFirebase(product)
    }

}