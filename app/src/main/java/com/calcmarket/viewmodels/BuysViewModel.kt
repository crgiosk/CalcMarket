package com.calcmarket.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcmarket.R
import com.calcmarket.core.PreferencesHelper
import com.calcmarket.data.usecase.BuyUseCase
import com.calcmarket.ui.binds.BuyBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BuysViewModel @Inject constructor(
    private val buyUseCase: BuyUseCase,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _fullBuysMutableLiveData = MutableLiveData<List<BuyBinding>>()
    val fullBuysMutableLiveData: LiveData<List<BuyBinding>> = _fullBuysMutableLiveData

    private val _lastLogin: MutableLiveData<String> = MutableLiveData(
        preferencesHelper.context.getString(R.string.las_login, formatDate(preferencesHelper.lastLogin))
    )
    val lastLogin: LiveData<String> = _lastLogin

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
        viewModelScope.launch(Dispatchers.IO) {
            buyUseCase.getFullBuys().collect { list ->
                val listToBinding = list.map { it.toBinding() }
                _fullBuysMutableLiveData.postValue(listToBinding)
            }
        }
    }

    fun deleteBuyInProgress(buyId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            buyUseCase.deleteBuyInProgress(buyId)
            withContext(Dispatchers.Main) {
                onSuccess.invoke()
            }
        }
    }

    fun updateLastLogin() {

        preferencesHelper.lastLogin = System.currentTimeMillis()

    }


    private fun formatDate(timestamp: Long): String {
        val sdfDayOfWeek = SimpleDateFormat("EEEE", Locale("es", "ES"))
        val dayOfWeek = sdfDayOfWeek.format(Date(timestamp))

        val sdfDay = SimpleDateFormat("d", Locale("es", "ES"))
        val day = sdfDay.format(Date(timestamp))

        val sdfMonth = SimpleDateFormat("MMM", Locale("es", "ES"))
        val month = sdfMonth.format(Date(timestamp))

        val sdfYear = SimpleDateFormat("yyyy", Locale("es", "ES"))
        val year = sdfYear.format(Date(timestamp))

        return "$dayOfWeek $day/$month/$year"
    }

    //move this and create his own viewModel BuyInProgress or NewBuyViewModel

}