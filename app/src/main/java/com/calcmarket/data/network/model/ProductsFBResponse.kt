package com.calcmarket.data.network.model

import com.calcmarket.ui.binds.ProductBinding

data class ProductsFBResponse(
    var id: String? = null,
    val name: String? = null,
    val costItem: Long? = null,
    val isFavorite: Boolean? = null,
    var type: String? = null,
) {

    fun toProductBinding(): ProductBinding {
        return ProductBinding(
            id = id.orEmpty(),
            idFireBase = id.orEmpty(),
            name = "$type $name",
            costItem = costItem?.toInt() ?: 0,
            isFavorite = isFavorite ?: false

        )
    }

}