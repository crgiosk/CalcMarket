package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.usecase.BuyUseCase
import com.calcmarket.ui.binds.BuyBinding
import com.calcmarket.ui.binds.ProductBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BuysViewModel @Inject constructor(
    private val buyUseCase: BuyUseCase
) : ViewModel() {

    private val _fullBuysMutableLiveData = MutableLiveData<List<BuyBinding>>()
    val fullBuysMutableLiveData: LiveData<List<BuyBinding>> = _fullBuysMutableLiveData

    private val _buySelectedMutableLiveData = MutableLiveData<BuyBinding>()
    val buySelectedLiveData: LiveData<BuyBinding> = _buySelectedMutableLiveData

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

    fun getFullBuys() {
        viewModelScope.launch {
            buyUseCase.getFullBuys().collect { list ->
                _fullBuysMutableLiveData.value = list.map { it.toBinding() }
            }
        }
    }

    fun getProductsByBuy(
        idBuy: Int,
        onSuccess: (items: List<ProductBinding>) -> Unit
    ) {
        viewModelScope.launch {
            buyUseCase.getProductsByBuy(idBuy).collect { productList ->
                onSuccess(productList.map { it.toBinding() } )
            }
        }
    }

    fun saveProductBuy(product: ProductBinding) {

        //todo:: se procede a guardar un producto cada que se toca el boton de agregar
        //todo:: tener en cuenta la actualizacion del producto (sumar, restar o eliminarlo)
        viewModelScope.launch(Dispatchers.IO) {
            buyUseCase.saveProductsByBuy(
                listOf(product.toEntity(buySelectedLiveData.value?.id ?: -1))
            )
        }
    }

}