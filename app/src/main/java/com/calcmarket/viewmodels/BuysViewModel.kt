package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.usecase.BuyUseCase
import com.calcmarket.data.usecase.ProductUseCase
import com.calcmarket.ui.binds.BuyBinding
import com.calcmarket.ui.binds.ProductBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BuysViewModel @Inject constructor(
    private val buyUseCase: BuyUseCase,
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val fullBuysMutableLiveData = MutableLiveData<List<BuyBinding>>()
    private val nameProductsMutableLiveData = MutableLiveData<List<String>>()

    fun fullBuysLiveData(): LiveData<List<BuyBinding>> = fullBuysMutableLiveData
    fun nameProductsLiveData(): LiveData<List<String>> = nameProductsMutableLiveData

    fun saveBuy(items: MutableList<ProductBinding>) {

        viewModelScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            val formatter = SimpleDateFormat("dd/MMM/yyyy hh:mm:ss", Locale.ROOT)
            val formatted = "Compra del dia ${formatter.format(calendar.time)}"
            val idBuy = calendar.timeInMillis.toInt()
            buyUseCase.saveLocalBuy(
                BuyEntity(
                    id = idBuy,
                    name = formatted,
                    countItems = items.count(),
                    total = items.sumOf { it.total }
                ),
                items.map { it.id }
            )
        }

    }
    fun saveProduct(productBinding: ProductBinding){
        //TODO!! remove idBuy
        productUseCase.saveProduct(productBinding.toEntity(1))
    }

    fun getFullBuys() {
        viewModelScope.launch {
            buyUseCase.getFullBuys().collect { buysList ->
                fullBuysMutableLiveData.postValue(
                    buysList.map { it.toBinding() }
                )
            }
        }
    }

    fun getProductByQuery(query: String) {
        viewModelScope.launch {
            productUseCase.getProductByQuery(query).collect {
                nameProductsMutableLiveData.postValue(it)
            }
        }
    }


}