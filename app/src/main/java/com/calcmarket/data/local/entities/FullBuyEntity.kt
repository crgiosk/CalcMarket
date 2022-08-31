package com.calcmarket.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.calcmarket.ui.binds.BuyBinding

data class FullBuyEntity(

    @Embedded val buy: BuyEntity,

    @Relation(
        parentColumn = BuyEntity.BUY_ID,
        entityColumn = ProductEntity.FK_BUY_ID
    )
    val products: List<ProductEntity>
) {
    fun toBinding() = BuyBinding(
        id = buy.id,
        name = buy.name,
        totalBuyValue = buy.total,
        items = products.map { it.toBinding() }

    )
}