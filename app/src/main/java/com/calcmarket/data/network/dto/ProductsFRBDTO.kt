package com.calcmarket.data.network.dto

import com.calcmarket.ui.binds.ProductBinding

/*
* Data
* Transfer
* Object
* this object is used to sen data to Firebase
* */
data class ProductsFRBDTO(
    var id: String? = null,
    var type: String? = null,
    val name: String? = null,
    val costItem: Double? = null,
    val unitMeasure: String? = null,
    val isFavorite: Boolean? = null
)
