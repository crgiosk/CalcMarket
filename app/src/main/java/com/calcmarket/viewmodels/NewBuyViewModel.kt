package com.calcmarket.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.usecase.BuyUseCase
import com.calcmarket.ui.binds.BuyBinding
import com.calcmarket.ui.binds.ProductBinding
import com.calcmarket.ui.binds.ProductsBuyBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NewBuyViewModel @Inject constructor(
    private val buyUseCase: BuyUseCase
) : ViewModel() {

    var currentProductSelected = ProductsBuyBinding()

    private val _buySelectedMutableLiveData = MutableLiveData<BuyBinding?>()
    val buySelectedLiveData: LiveData<BuyBinding?> = _buySelectedMutableLiveData

    private val _productsByBuyLiveData = MediatorLiveData<List<ProductsBuyBinding>>().apply {
        addSource(buySelectedLiveData) { myBuy ->
            viewModelScope.launch (Dispatchers.IO) {
                myBuy?.let { buy ->
                    buyUseCase.getProductsByBuy(buy.id).collect { products ->
                        val productsBinding = products.map { it.toProductsBuyBinding() }
                        postValue(productsBinding)
                    }
                }
            }
        }
    }

    val productsByBuyLiveData: LiveData<List<ProductsBuyBinding>> get() = _productsByBuyLiveData

    fun buySelectedSet(buy: BuyBinding) {
        _buySelectedMutableLiveData.value = buy
    }

    fun createNewBuy() {

        viewModelScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            val formatter = SimpleDateFormat("dd/MMM/yyyy hh:mm:ss", Locale.ROOT)
            val nameBuy = "Compra del dia ${formatter.format(calendar.time)}"
            val idBuy = calendar.timeInMillis.toInt()
            buyUseCase.saveLocalBuy(
                BuyEntity(id = idBuy, name = nameBuy, isInProgress = true)
            )

            withContext(Dispatchers.Main) {
                _buySelectedMutableLiveData.value = BuyBinding(id = idBuy, name = nameBuy)
            }
        }

    }

    fun updateItemBuy(product: ProductsBuyBinding) {
        viewModelScope.launch(Dispatchers.IO) {
            buySelectedLiveData.value?.let {
                val productsByBuyEntity = product.toEntity(it.id)
                buyUseCase.updateCostAmountItemBuy(productsByBuyEntity).collect { result ->
                    result
                        .onSuccess {
                            Log.d("RoomFlow", "Item actualizado correctamente")
                        }.onFailure { exception ->
                            Log.e(
                                "RoomFlow",
                                "Error al actualizar el item: ${exception.message}",
                                exception
                            )
                        }
                }
            }
        }
    }

    fun deleteItemBuy(product: ProductsBuyBinding) {
        viewModelScope.launch(Dispatchers.IO) {
            buySelectedLiveData.value?.let {
                val productEntity = product.toEntity(it.id)
                buyUseCase.deleteItemBuy(productEntity)
                    .collect { result ->
                        result
                            .onSuccess {
                                Log.d("RoomFlow", "Item actualizado correctamente")
                            }.onFailure { exception ->
                                Log.e(
                                    "RoomFlow",
                                    "Error al actualizar el item: ${exception.message}",
                                    exception
                                )
                            }
                    }
            }
        }
    }

    fun updateItemsBuy(idBuy: Int, items: MutableList<ProductsBuyBinding>) {

        viewModelScope.launch(Dispatchers.IO) {
            buyUseCase.updateItemsAndCostItemsBuy(
                idBuy = idBuy,
                countItems = items.sumOf { it.amount },
                total = items.sumOf { it.total }
            )
        }
    }

    fun checkBuyInProgress(onComplete: (BuyBinding?) -> Unit) {
        viewModelScope.launch {
            val buy = withContext(Dispatchers.IO) {
                buyUseCase.checkBuyInProgress()
            }?.toBinding()
            withContext(Dispatchers.Main) {
                onComplete.invoke(buy)
            }
        }
    }

    fun saveProductBuy(product: ProductsBuyBinding, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val itemsToSave = product.toEntity(buySelectedLiveData.value?.id ?: -1)
            buyUseCase.saveProductsByBuy(
                listOf(itemsToSave)
            ).catch {
                println("saveProductBuy \n${it.printStackTrace()}")
            }.collect {
                //hideLoading
                onComplete?.invoke()
            }
        }
    }

    fun updateProductSelectedData(
        product: ProductBinding,
        total: Int,
        value: Int,
        amount: Int
    ) {
        currentProductSelected.apply {
            this.productId = product.id
            this.total = total
            this.costItem = value
            this.amount = amount
            this.type = product.type
            this.measure = product.measure
        }
    }

}