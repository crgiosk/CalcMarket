package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calcmarket.data.local.entities.ProductEntity
import com.calcmarket.data.usecase.ProductUseCase
import com.calcmarket.ui.binds.ProductBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productUseCase: ProductUseCase) : ViewModel() {

    var currentProduct = ProductBinding()
    private val nameProductsMutableLiveData = MutableLiveData<List<ProductBinding>>()

    fun nameProductsLiveData(): LiveData<List<ProductBinding>> = nameProductsMutableLiveData

    fun saveProduct(onSave: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = productUseCase.saveProduct(
                ProductEntity(name = currentProduct.name, costProduct = currentProduct.costItem)
            )
            if (result.toInt() > 0){
                onSave?.invoke()
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