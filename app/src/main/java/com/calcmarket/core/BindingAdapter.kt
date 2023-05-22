package com.calcmarket.core

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.calcmarket.R

@BindingAdapter("android:build_coin_format")
fun buildCoinFormat(view: TextView, value: Int) {
    view.text = Extensions.buildCoinFormat(value)
}

@BindingAdapter("android:product_total_value")
fun textTotalValue(view: TextView, value: Int) {
    val valueFormated = Extensions.buildCoinFormat(value)
    view.text = view.context.getString(R.string.product_total_value, valueFormated)
}

@BindingAdapter("android:product_unit_value")
fun textUnitValue(view: TextView, value: Int) {
    val valueFormated = Extensions.buildCoinFormat(value)
    view.text = view.context.getString(R.string.product_unit_value, valueFormated)
}

@BindingAdapter("android:text_total_products")
fun textTotalProducts(view: TextView, total: Int) {
    view.text = view.context.getString(R.string.total_products, total.toString())
}

@BindingAdapter("android:any_to_string")
fun anyToString(view: TextView, value: Any) {
    view.text = value.toString()
}