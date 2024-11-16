package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.data.network.dto.ProductsFRBDTO
import com.calcmarket.domain.GetProductsUseCase
import com.calcmarket.domain.SaveProductsUseCase
import com.calcmarket.ui.binds.ProductBinding
import com.calcmarket.ui.binds.ProductsBuyBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val saveProductsUseCase: SaveProductsUseCase
) : ViewModel() {

    var currentProduct = ProductsBuyBinding()

    private val currentProducts = mutableListOf<ProductBinding>()
    private val nameProductsMutableLiveData = MutableLiveData<List<ProductsBuyBinding>>()

    fun nameProductsLiveData(): LiveData<List<ProductsBuyBinding>> = nameProductsMutableLiveData

    fun getProductByName(name: String, onSuccessAction: (ProductsBuyBinding?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val product = currentProducts.find { it.name.equals(name, true) }
                onSuccessAction(product?.toProductsBuyBinding())
            }
        }
    }

    fun getProductByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val products = currentProducts.filter {
                val isValidName = it.name.contains(query, ignoreCase = true)
                val isValidType = it.type.contains(query, ignoreCase = true)
                isValidName || isValidType
            }.sortedByDescending { it.name }

            val resultFiltered = products.map { it.toProductsBuyBinding() }
            nameProductsMutableLiveData.postValue(resultFiltered)
        }
    }

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase.invoke().collect {
                currentProducts.clear()
                currentProducts.addAll(it)
                nameProductsMutableLiveData.postValue(
                    currentProducts.map { product ->
                        product.toProductsBuyBinding()
                    }
                )
            }
        }
    }

    fun saveNewProductInFirebase(
        name: String,
        typeProduct: String,
        costItem: Double,
        isFavorite: Boolean,
        measure: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = ProductsFRBDTO(
                name = name,
                type = typeProduct,
                isFavorite = isFavorite,
                costItem = costItem,
                unitMeasure = measure
            )
            saveProductsUseCase.invoke(product)
        }
    }


}