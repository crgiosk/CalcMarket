package com.calcmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.core.Extensions
import com.calcmarket.core.Extensions.hideKeyboard
import com.calcmarket.core.Extensions.showKeyboard
import com.calcmarket.databinding.ActivityMainBinding
import com.calcmarket.ui.adapter.BuyAdapter
import com.calcmarket.ui.binds.BuyBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val buyAdapter: BuyAdapter by lazy {
        BuyAdapter {
            binding.totalBuy.text = Extensions.buildCoinFormat(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, true)
            adapter = buyAdapter
        }


        binding.amountEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {

                val count = Extensions.removeCoinSymbol(editable.toString())
                val itemValue = Extensions.removeCoinSymbol(binding.valueEditText.text.toString())

                binding.amountEditText.removeTextChangedListener(this)

                if (itemValue.isNotEmpty()) {
                    if (count == "$" || count.isEmpty() || count.isBlank() || count == "" || count == "0") {
                        binding.totalEditText.setText("")
                    } else {
                        calculateAndShowPrice()
                    }
                }
                binding.amountEditText.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        binding.valueEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {
                val text = Extensions.removeCoinSymbol(editable.toString())

                binding.valueEditText.removeTextChangedListener(this)
                if (text == "$" || text == "" || text == "0") {
                    binding.valueEditText.setText("")
                } else {
                    binding.valueEditText.setText(
                        Extensions.buildCoinFormat(text.toInt())
                    )
                    binding.valueEditText.setSelection(
                        binding.valueEditText.text.toString().length
                    )
                    if (binding.amountEditText.text?.isNotEmpty() == true){
                        calculateAndShowPrice()

                    } else {
                        binding.totalEditText.setText("")
                    }
                }
                binding.valueEditText.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        binding.addProduct.setOnClickListener { button ->
            val touchables = binding.formInputs.touchables.filterIsInstance<EditText>()
            if (touchables.none { it.text.isEmpty() }) {
                val total = Extensions.removeCoinSymbol(binding.totalEditText.text.toString()).toInt()
                val value = Extensions.removeCoinSymbol(binding.valueEditText.text.toString()).toInt()
                buyAdapter.addItem(
                    BuyBinding(
                        id = buyAdapter.itemCount + 1,
                        name = binding.orderNameEditText.text?.toString() ?: String(),
                        amount = binding.amountEditText.text?.toString()?.toInt() ?: 0,
                        total = total,
                        costItem = value,
                    )
                )
                button.hideKeyboard()
                binding.formInputs.touchables.filterIsInstance<EditText>().forEach { editText ->
                    editText.setText("")
                }
                binding.recyclerViewOrders.smoothScrollToPosition(buyAdapter.itemCount)
                binding.orderNameEditText.requestFocus()
            } else {
                touchables.filter { it.text.toString().isEmpty() }.forEach {
                    it.error = "Rellenar esta opcion."
                }
            }
        }

        binding.orderNameEditText.requestFocus()
        binding.orderNameEditText.showKeyboard()
    }

    private fun calculateAndShowPrice() {

        val count = Extensions.removeCoinSymbol(binding.amountEditText.text.toString())
        val itemValue = Extensions.removeCoinSymbol(binding.valueEditText.text.toString())
        binding.totalEditText.setText("")
        binding.totalEditText.setText(
            Extensions.buildCoinFormat(count.toInt() * itemValue.toInt())
        )
    }
}