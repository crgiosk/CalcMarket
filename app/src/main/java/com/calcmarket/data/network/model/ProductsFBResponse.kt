package com.calcmarket.data.network.model

import com.calcmarket.ui.binds.ProductBinding

data class ProductsFBResponse(
    var id: String? = null,
    val name: String? = null,
    val costItem: Long? = null,
    val favorite: Boolean? = null,
    var type: String? = null,
    var unitMeasure: String? = null,

    ) {

    fun toProductBinding(): ProductBinding {
        return ProductBinding(
            id = 0,
            idFireBase = id.orEmpty(),
            name = name.orEmpty(),
            costItem = costItem?.toInt() ?: 0,
            isFavorite = favorite ?: false,
            measure = unitMeasure ?: "",
            type = type.orEmpty()
        )
    }

}