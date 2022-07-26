package com.calcmarket.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.core.Extensions
import com.calcmarket.core.Extensions.hideKeyboard
import com.calcmarket.core.Extensions.showKeyboard
import com.calcmarket.databinding.FragmentNewBuyBinding
import com.calcmarket.ui.adapter.BuyAdapter
import com.calcmarket.ui.adapter.ProductAutoCompleteAdapter
import com.calcmarket.ui.binds.ProductBinding
import com.calcmarket.viewmodels.BuysViewModel

class NewBuyFragment : Fragment() {

    private lateinit var binding: FragmentNewBuyBinding
    private val buyAdapter: BuyAdapter by lazy {
        BuyAdapter {
            binding.totalBuy.text = Extensions.buildCoinFormat(it)
            binding.buttonSaveBuy.visibility = if (it > 0) View.VISIBLE else View.GONE
        }
    }

    private val autoCompleteAdapter: ProductAutoCompleteAdapter by lazy {
        ProductAutoCompleteAdapter {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val viewModel: BuysViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewBuyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, true)
            adapter = buyAdapter
        }


        binding.nameProduct.setAdapter(autoCompleteAdapter)
        setupListeners()
        setupObservers()

        binding.nameProduct.requestFocus()
        binding.nameProduct.showKeyboard()
    }

    private fun setupObservers() {
        viewModel.nameProductsLiveData().observe(viewLifecycleOwner) {
            autoCompleteAdapter.updateItems(it)
        }
    }

    private fun setupListeners() {
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
                    if (binding.amountEditText.text?.isNotEmpty() == true) {
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
            addProduct()
        }

        binding.buttonSaveBuy.setOnClickListener {
            viewModel.saveBuy(buyAdapter.getData())
        }

        binding.amountEditText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addProduct()
            }
            false
        }

        binding.nameProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty() == true){
                    viewModel.getProductByQuery(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.nameProduct.setOnItemClickListener { parent, _, position, _ ->
            binding.nameProduct.setText((parent.getItemAtPosition(position) as String))
            binding.nameProduct.setSelection(binding.nameProduct.text.length)
            binding.valueEditText.requestFocus()
        }
    }

    private fun addProduct() {
        val editTexts = binding.formInputs.touchables.filterIsInstance<EditText>()
        if (editTexts.none { it.text.isEmpty() }) {
            val total = Extensions.removeCoinSymbol(binding.totalEditText.text.toString()).toInt()
            val value = Extensions.removeCoinSymbol(binding.valueEditText.text.toString()).toInt()
            buyAdapter.addItem(
                ProductBinding(
                    id = buyAdapter.itemCount + 1,
                    name = binding.nameProduct.text?.toString() ?: String(),
                    amount = binding.amountEditText.text?.toString()?.toInt() ?: 0,
                    total = total,
                    costItem = value,
                )
            )
            binding.formInputs.touchables.filterIsInstance<EditText>().forEach { editText ->
                editText.setText("")
            }
            binding.recyclerViewOrders.smoothScrollToPosition(buyAdapter.itemCount)
            binding.nameProduct.requestFocus()
        } else {
            editTexts.filter { it.text.toString().isEmpty() }.forEach {
                it.error = "Rellenar esta opcion."
            }
        }
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