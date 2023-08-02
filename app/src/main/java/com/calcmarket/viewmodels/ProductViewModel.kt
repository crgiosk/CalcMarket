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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productUseCase: ProductUseCase) : ViewModel() {

    var currentProduct = ProductBinding()
    private val nameProductsMutableLiveData = MutableLiveData<List<ProductBinding>>()

    fun nameProductsLiveData(): LiveData<List<ProductBinding>> = nameProductsMutableLiveData

    fun newProduct(onSuccessAction: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                productUseCase.saveProduct(
                    ProductEntity(name = currentProduct.name, costProduct = currentProduct.costItem)
                )
            }
            val id = result.await().toInt()
            if (id > 0){
                currentProduct.id = id
                withContext(Dispatchers.Main) {
                    onSuccessAction(id)
                }
            }
        }
    }

    fun getProductByName(name: String, onSuccessAction: (ProductBinding?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = productUseCase.getProductByName(name)?.toBinding()
            withContext(Dispatchers.Main) {
                onSuccessAction(product)
            }
        }
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