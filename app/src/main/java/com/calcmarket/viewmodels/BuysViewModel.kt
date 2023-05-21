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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BuysViewModel @Inject constructor(
    private val buyUseCase: BuyUseCase
) : ViewModel() {

    private val fullBuysMutableLiveData = MutableLiveData<List<BuyBinding>>()

    fun fullBuysLiveData(): LiveData<List<BuyBinding>> = fullBuysMutableLiveData

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
                )
            )
            buyUseCase.saveProductsByBuy(
                items.map { it.toEntity(idBuy) }
            )
        }

    }

    fun getFullBuys() {
        viewModelScope.launch {
            buyUseCase.getFullBuys().collect { list ->
                fullBuysMutableLiveData.value = list.map { it.toBinding() }
            }
        }
    }


}