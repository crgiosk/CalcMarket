package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.data.network.dto.ProductsFRBDTO
import com.calcmarket.data.usecase.ProductUseCase
import com.calcmarket.domain.GetProductsUseCase
import com.calcmarket.domain.SaveFirebaseProductsUseCase
import com.calcmarket.ui.binds.ProductBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val localProductsUseCase: ProductUseCase,
    private val saveFirebaseProductsUseCase: SaveFirebaseProductsUseCase
) : ViewModel() {

    private val productsInLocalDB = mutableListOf<ProductBinding>()

    private val _productsToShowBySearch = MutableLiveData<List<ProductBinding>>()
    fun productsToShowBySearch(): LiveData<List<ProductBinding>> = _productsToShowBySearch

    fun getLocalProductByName(name: String, onSuccessAction: (ProductBinding?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = productsInLocalDB.find { it.name.equals(name, true) }
            withContext(Dispatchers.Main) {
                onSuccessAction(product)
            }
        }
    }

    fun getProductByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val products = productsInLocalDB.filter {
                val isValidName = it.name.contains(query, ignoreCase = true)
                val isValidType = it.type.contains(query, ignoreCase = true)
                isValidName || isValidType
            }

            val resultSorted = products.sortedByDescending { it.name }

            _productsToShowBySearch.postValue(resultSorted)
        }
    }

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase.invoke().collect {
                compareAndSaveProductsInLocalDB(it)
            }
        }
    }

    private suspend fun compareAndSaveProductsInLocalDB(productsFirebase: List<ProductBinding>) {
        return withContext(Dispatchers.IO) {
            val localProducts = localProductsUseCase.getAllProducts().first()
            val diff = productsFirebase.filter { it.name !in localProducts.map { item -> item.name } }
            if (diff.isNotEmpty()) {
                val entityProducts = diff.map { it.toEntity() }
                localProductsUseCase.saveProducts(entityProducts)
                    .onCompletion {

                    }
                    .catch {

                    }.collect { result ->
                        if (result.none { it == 0L }) {
                            val newLocalProducts = localProductsUseCase.getAllProducts().first()
                            val bindingProducts = newLocalProducts.map { it.toBinding() }
                            setNewCurrentProducts(bindingProducts)
                        }
                    }
            } else {
                val productsBinding = localProducts.map { it.toBinding() }
                setNewCurrentProducts(productsBinding)
            }
/*

            val difference = uniqueItemsInLocal + uniqueItemsInFirebase

            difference*/
            //comparar los productos locales con los de firebase y guardar/comparar
            //para que no ocurra un error al guardar por foreign key
        }
    }

    private fun setNewCurrentProducts(products: List<ProductBinding>) {
        productsInLocalDB.clear()
        productsInLocalDB.addAll(products)
    }

    fun saveNewProductInFirebase(
        name: String,
        typeProduct: String,
        costItem: Double,
        isFavorite: Boolean,
        measure: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = ProductsFRBDTO(
                name = name,
                type = typeProduct,
                isFavorite = isFavorite,
                costItem = costItem,
                unitMeasure = measure
            )
            saveFirebaseProductsUseCase.invoke(product).collect {
                //delay(2000)
                onComplete.invoke()
            }
        }
    }


}
