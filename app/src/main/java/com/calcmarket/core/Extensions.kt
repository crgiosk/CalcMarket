package com.calcmarket.core

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.properties.Delegates

object Extensions  {
    fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun buildCoinFormat(number: Int): String {
        val symbol = DecimalFormatSymbols()
        symbol.groupingSeparator = '.'
        symbol.decimalSeparator = ','
        val format = DecimalFormat( "$#,###.##", symbol)
        return format.format(number)
    }

    fun removeCoinSymbol(value: String): String =
        value.replace("$","")
            .replace(".","")
            .replace(",","")

    fun <VH : RecyclerView.ViewHolder, T> RecyclerView.Adapter<VH>.basicDiffUtil(
        initialValue: MutableList<T> = mutableListOf(),
        areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
        areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
    ) =
        Delegates.observable(initialValue) { _, old, new ->
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = old.size

                override fun getNewListSize(): Int = new.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    areItemsTheSame(old[oldItemPosition], old[newItemPosition])

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    areContentsTheSame(old[oldItemPosition], old[newItemPosition])

            })
        }

}