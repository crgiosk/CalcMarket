package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.domain.GetProductsUseCase
import com.calcmarket.ui.binds.ProductBinding
import com.calcmarket.ui.binds.ProductsBuyBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
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
            val product = currentProducts.filter { it.name.contains(query, true) }.sortedByDescending { it.name }
            val resultFiltered = product.map { it.toProductsBuyBinding() }
            nameProductsMutableLiveData.postValue(resultFiltered)
        }
    }

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase.invoke().collect {
                currentProducts.clear()
                currentProducts.addAll(it)
            }
        }
    }


}