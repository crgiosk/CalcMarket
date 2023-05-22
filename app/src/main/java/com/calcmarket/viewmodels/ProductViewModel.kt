package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.data.local.entities.ProductEntity
import com.calcmarket.data.usecase.ProductUseCase
import com.calcmarket.ui.binds.ProductBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productUseCase: ProductUseCase) : ViewModel() {

    var currentProduct = ProductBinding()
    private val nameProductsMutableLiveData = MutableLiveData<List<ProductBinding>>()
    private val _newProductSaved = MutableLiveData<ProductBinding?>()

    fun nameProductsLiveData(): LiveData<List<ProductBinding>> = nameProductsMutableLiveData
    fun newProductSavedLiveData(): LiveData<ProductBinding?> = _newProductSaved

    fun saveProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                productUseCase.saveProduct(
                    ProductEntity(name = currentProduct.name, costProduct = currentProduct.costItem)
                )
            }
            val id = result.await().toInt()
            if (id > 0){
                currentProduct.id = id
                _newProductSaved.postValue(currentProduct)
            }
        }
    }

    fun resetNewProductValue() {
        _newProductSaved.value = null
    }

    fun getProductByQuery(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            productUseCase.getProductByQuery(query).collect { items ->
                nameProductsMutableLiveData.postValue(
                    items.map { it.toBinding() }
                )
            }
        }
    }


}