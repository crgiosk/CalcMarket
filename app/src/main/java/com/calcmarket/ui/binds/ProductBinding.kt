package com.calcmarket.ui.binds

import com.calcmarket.data.network.dto.ProductsFRBDTO

data class ProductBinding(
    var id: String = "",
    val name: String = "",
    var costItem: Int = 0,
    val isFavorite: Boolean = false,
    val idFireBase: String = ""
) {
    override fun toString(): String {
        return "id = $id\n" +
                "name = $name\n" +
                "costItem = $costItem\n" +
                "isFavorite = $isFavorite\n"

    }

    fun toDTO(): ProductsFRBDTO {
        return ProductsFRBDTO(
            id = id,
            name = "name",
            //costItem = costItem.toString(),
            isFavorite = isFavorite,
        )
    }

    fun toProductsBuyBinding(): ProductsBuyBinding {
        return ProductsBuyBinding(
            name = name,
            costItem = costItem,
            isFavorite = isFavorite
        )
    }
}