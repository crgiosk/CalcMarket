package com.calcmarket.ui.binds

import com.calcmarket.data.local.entities.ProductEntity
import com.calcmarket.data.network.dto.ProductsFRBDTO

data class ProductBinding(
    var id: Int = 0,
    val name: String = "",
    var costItem: Int = 0,
    val isFavorite: Boolean = false,
    val idFireBase: String = "",
    var type: String = "",
    var measure: String = "",
) {
    override fun toString(): String {
        return "id = $id\n" +
                "name = $name\n" +
                "costItem = $costItem\n" +
                "isFavorite = $isFavorite\n"

    }

    fun toDTO(): ProductsFRBDTO {
        return ProductsFRBDTO(
            name = name,
            type = type,
            costItem = costItem.toDouble(),
            isFavorite = isFavorite,
            unitMeasure = measure
        )
    }

    fun toProductsBuyBinding(): ProductsBuyBinding {
        return ProductsBuyBinding(
            name = name,
            productId = id,
            costItem = costItem,
            isFavorite = isFavorite,
            type = type,
            measure = measure
        )
    }

    fun toEntity(): ProductEntity {
        return ProductEntity(
            name = name,
            type = type,
            measure = measure,
            costProduct = costItem,
            isFavorite = isFavorite
        )
    }
}