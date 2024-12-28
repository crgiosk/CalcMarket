package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.data.usecase.BuyUseCase
import com.calcmarket.ui.binds.BuyBinding
import com.calcmarket.ui.binds.ProductsBuyBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BuyDetailViewModel @Inject constructor(
    private val buyUseCase: BuyUseCase
): ViewModel() {

    private val _buySelectedMutableLiveData = MutableLiveData<BuyBinding?>()
    val buySelectedLiveData: LiveData<BuyBinding?> = _buySelectedMutableLiveData

    val productsByBuyLiveData: LiveData<List<ProductsBuyBinding>> get() = _productsByBuyLiveData
    private val _productsByBuyLiveData = MediatorLiveData<List<ProductsBuyBinding>>().apply {
        sourceProductsBuyLiveData()
    }

    private fun MediatorLiveData<List<ProductsBuyBinding>>.sourceProductsBuyLiveData() {
        addSource(buySelectedLiveData) { myBuy ->
            viewModelScope.launch(Dispatchers.IO) {
                myBuy?.let { buy ->
                    buyUseCase.getProductsByBuy(buy.id).collect { products ->
                        val productsBinding = products.map { it.toProductsBuyBinding() }
                        postValue(productsBinding)
                    }
                }
            }
        }
    }


    fun checkBuyInProgress() {
        viewModelScope.launch {
            val buy = withContext(Dispatchers.IO) {
                buyUseCase.checkBuyInProgress()
            }?.toBinding()
            _buySelectedMutableLiveData.postValue(buy)
        }
    }

}