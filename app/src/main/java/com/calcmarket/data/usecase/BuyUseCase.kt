package com.calcmarket.data.usecase

import com.calcmarket.data.local.repository.BuyRepository
import com.calcmarket.data.local.entities.BuyEntity
import javax.inject.Inject

class BuyUseCase @Inject constructor(
    private val repository: BuyRepository
) {
    fun saveLocalBuy(
        buyEntity: BuyEntity,
        productEntity: List<Int>
    ) = repository.saveLocalBuy(buyEntity, productEntity)

    fun getFullBuys() = repository.getFullBuys()
}