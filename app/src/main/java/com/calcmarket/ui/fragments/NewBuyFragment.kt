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
import com.calcmarket.MainActivity
import com.calcmarket.R
import com.calcmarket.core.Extensions.buildCoinFormat
import com.calcmarket.core.Extensions.removeCoinSymbol
import com.calcmarket.core.Extensions.showAlertConfirmationDialog
import com.calcmarket.core.Extensions.showKeyboard
import com.calcmarket.databinding.FragmentNewBuyBinding
import com.calcmarket.ui.adapter.BuyAdapter
import com.calcmarket.ui.adapter.ProductAutoCompleteAdapter
import com.calcmarket.ui.binds.ProductBinding
import com.calcmarket.viewmodels.BuysViewModel
import com.calcmarket.viewmodels.ProductViewModel

class NewBuyFragment : Fragment() {


    private val viewModel: BuysViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()

    private lateinit var binding: FragmentNewBuyBinding
    private val buyAdapter: BuyAdapter by lazy {
        BuyAdapter {
            binding.totalBuy.text = buildCoinFormat(it)
            binding.buttonSaveBuy.visibility = if (it > 0) View.VISIBLE else View.GONE
        }
    }

    private val autoCompleteAdapter: ProductAutoCompleteAdapter by lazy {
        ProductAutoCompleteAdapter {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewBuyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
        setupObservers()
        setDefaultFocus()
    }

    private fun setupUI() {
        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, true)
            adapter = buyAdapter
        }

        binding.nameProduct.setAdapter(autoCompleteAdapter)
        productViewModel.currentProduct = ProductBinding()
    }

    private fun setDefaultFocus() {
        binding.nameProduct.requestFocus()
        binding.nameProduct.showKeyboard()
    }

    private fun setupObservers() {
        productViewModel.nameProductsLiveData().observe(viewLifecycleOwner) {
            if (isVisible) autoCompleteAdapter.updateItems(it)
        }
        productViewModel.newProductSavedLiveData().observe(viewLifecycleOwner) {
            if (isVisible && it != null ) {
                buyAdapter.updateItemId(it)
                productViewModel.resetNewProductValue()
            }
        }
    }

    private fun setupListeners() {
        binding.amountEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {

                val count = removeCoinSymbol(editable.toString())
                val itemValue = removeCoinSymbol(binding.valueEditText.text.toString())

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
                val text = removeCoinSymbol(editable.toString())

                binding.valueEditText.removeTextChangedListener(this)
                if (text == "$" || text == "" || text == "0") {
                    binding.valueEditText.setText("")
                } else {
                    binding.valueEditText.setText(
                        buildCoinFormat(text.toInt())
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

        binding.addProduct.setOnClickListener {
            addProduct()
        }

        binding.buttonSaveBuy.setOnClickListener {
            (requireActivity() as? MainActivity)?.showAlertConfirmationDialog(
                message = requireContext().getString(R.string.are_you_sure),
                onAgree = {
                    viewModel.saveBuy(buyAdapter.getData())
                    requireActivity().onBackPressed()
                }
            )
        }

        binding.amountEditText.setOnEditorActionListener { _, actionId, _ ->
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
                    productViewModel.getProductByQuery(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.nameProduct.setOnItemClickListener { parent, _, position, _ ->
            val itemSelected = parent.getItemAtPosition(position) as ProductBinding
            binding.nameProduct.setText(itemSelected.name)
            binding.nameProduct.setSelection(binding.nameProduct.text.length)
            binding.valueEditText.requestFocus()

            productViewModel.currentProduct = itemSelected
            binding.valueEditText.setText(
                buildCoinFormat(itemSelected.costItem)
            )
        }
    }

    private fun addProduct() {
        val editTexts = binding.formInputs.touchables.filterIsInstance<EditText>()
        if (editTexts.none { it.text.isEmpty() }) {
            val total = removeCoinSymbol(binding.totalEditText.text.toString()).toInt()
            val value = removeCoinSymbol(binding.valueEditText.text.toString()).toInt()
            val amount = binding.amountEditText.text?.toString()?.toInt() ?: 0
            if (productViewModel.currentProduct.id == 0) {
                productViewModel.currentProduct = ProductBinding(
                        name = binding.nameProduct.text?.toString() ?: String(),
                        amount = amount,
                        total = total,
                        costItem = value,
                )
                productViewModel.saveProduct()
            } else {
                productViewModel.currentProduct.apply {
                    this.total = total
                    this.costItem = value
                    this.amount = amount
                }
            }
            buyAdapter.addItem(
                productViewModel.currentProduct
            )
            binding.formInputs.touchables.filterIsInstance<EditText>().forEach { editText ->
                editText.setText("")
            }
            binding.recyclerViewOrders.smoothScrollToPosition(buyAdapter.itemCount)
            binding.nameProduct.requestFocus()
            productViewModel.currentProduct = ProductBinding()
        } else {
            editTexts.filter { it.text.toString().isEmpty() }.forEach {
                it.error = "Rellenar esta opcion."
            }
        }
    }

    private fun calculateAndShowPrice() {

        val count = removeCoinSymbol(binding.amountEditText.text.toString())
        val itemValue = removeCoinSymbol(binding.valueEditText.text.toString())
        binding.totalEditText.setText("")
        binding.totalEditText.setText(
            buildCoinFormat(count.toInt() * itemValue.toInt())
        )
    }

}