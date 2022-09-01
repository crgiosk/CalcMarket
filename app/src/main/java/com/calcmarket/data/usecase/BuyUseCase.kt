package com.calcmarket.data.usecase

import com.calcmarket.data.local.BuyRepository
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductEntity
import com.calcmarket.ui.binds.BuyBinding
import javax.inject.Inject

class BuyUseCase @Inject constructor(
    private val repository: BuyRepository
) {

    fun saveLocalBuy(
        buyEntity: BuyEntity,
        productEntity: List<ProductEntity>
    ) = repository.saveLocalBuy(buyEntity, productEntity)

    fun getFullBuys() = repository.getFullBuys()

    fun getProductByQuery(query: String) = repository.getProductByQuery(query)
}