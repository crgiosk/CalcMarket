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
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.R
import com.calcmarket.core.Extensions.buildCoinFormat
import com.calcmarket.core.Extensions.removeCoinSymbol
import com.calcmarket.core.Extensions.showKeyboard
import com.calcmarket.data.local.di.DialogConfirm
import com.calcmarket.databinding.FragmentNewBuyBinding
import com.calcmarket.ui.adapter.BuyAdapter
import com.calcmarket.ui.adapter.ProductAutoCompleteAdapter
import com.calcmarket.ui.binds.ProductsBuyBinding
import com.calcmarket.viewmodels.BuysViewModel
import com.calcmarket.viewmodels.ProductViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewBuyFragment : Fragment() {

    @Inject
    lateinit var confirmDialog: DialogConfirm

    private val firebase = FirebaseCrashlytics.getInstance()

    private val viewModel: BuysViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()

    private lateinit var binding: FragmentNewBuyBinding
    private val buyAdapter: BuyAdapter by lazy {
        BuyAdapter(
            onUpdateProduct = {
                viewModel.updateItemBuy(it)
            },
            onDeleteProduct = {
                viewModel.deleteItemBuy(it)
            }
        )
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
        validateExistBuy()
        loadFirebaseProducts()
    }

    private fun validateExistBuy() {
        if (viewModel.buySelectedLiveData.value == null) {
            viewModel.createNewBuy()
        }
    }

    private fun setupUI() {
        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, true)
            adapter = buyAdapter
        }

        binding.nameProduct.setAdapter(autoCompleteAdapter)
        productViewModel.currentProduct = ProductsBuyBinding()
    }

    private fun setDefaultFocus() {
        binding.nameProduct.requestFocus()
        binding.nameProduct.showKeyboard()
    }

    private fun setupObservers() {
        productViewModel.nameProductsLiveData().observe(viewLifecycleOwner) {
            if (isVisible) autoCompleteAdapter.updateItems(it)
        }

        viewModel.productsByBuyLiveData.observe(viewLifecycleOwner) { products ->
            buyAdapter.submitList(products)
            updateAndShowTotalValue(products.sumOf { it.total })
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
                        binding.totalEditText.text?.clear()
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
                    binding.valueEditText.text?.clear()
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
                        binding.totalEditText.text?.clear()
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
            confirmDialog.showAlertConfirmationDialog(
                message = requireContext().getString(R.string.are_you_sure),
                onPositiveButton = {
                    viewModel.buySelectedLiveData.value?.let { buy ->
                        viewModel.updateItemsBuy(buy.id, buyAdapter.currentList)
                        requireActivity().onBackPressed()
                    }
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
                if (p0?.isNotEmpty() == true) {
                    productViewModel.getProductByQuery(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.nameProduct.setOnItemClickListener { parent, _, position, _ ->
            val itemSelected = parent.getItemAtPosition(position) as ProductsBuyBinding
            binding.nameProduct.setText(itemSelected.name)
            binding.nameProduct.setSelection(binding.nameProduct.text.length)
            binding.typeProductEditText.setText(itemSelected.type)
            binding.measureProductEditText.setText(itemSelected.measure)
            binding.valueEditText.requestFocus()

            productViewModel.currentProduct = itemSelected
            binding.valueEditText.setText(
                buildCoinFormat(itemSelected.costItem)
            )
        }
    }

    private fun addProduct() {
        if (checkIsValidNameAndType() && checkIsValidOthersInputs()) {
            val total = removeCoinSymbol(binding.totalEditText.text.toString()).toInt()
            val value = removeCoinSymbol(binding.valueEditText.text.toString()).toInt()
            val nameProduct = binding.nameProduct.text?.toString() ?: String()
            val amount = binding.amountEditText.text?.toString()?.toInt() ?: 0
            val type = binding.typeProductEditText.text?.toString() ?: ""
            val measure = binding.measureProductEditText.text?.toString() ?: ""

            firebase.setCustomKey("name_product", nameProduct)
            firebase.setCustomKey("cost_product", value)
            firebase.setCustomKey("amount_product", amount)
            firebase.sendUnsentReports()

            productViewModel.getProductByName(nameProduct) { product ->
                if (product != null) {
                    processExistProduct(product, total, value, amount)
                } else {
                    processNotExistProduct(nameProduct, amount, total, value)
                    saveProductInFB(nameProduct, value, type, measure, false)
                }
                clearAndResetForm(binding.formInputs.touchables.filterIsInstance<EditText>())
            }
        }
    }

    private fun checkIsValidOthersInputs(): Boolean {
        return when {
            (binding.valueEditText.text?.toString() ?: "").isEmpty() -> {
                binding.valueEditText.error = getString(R.string.complete_this_input)
                false
            }

            (binding.measureProductEditText.text?.toString() ?: "").isEmpty() -> {
                binding.measureProductEditText.error = getString(R.string.complete_this_input)
                false
            }

            (binding.amountEditText.text?.toString()?.toInt() ?: 0) == 0 -> {
                binding.amountEditText.error = getString(R.string.complete_this_input)
                false
            }

            else -> true
        }
    }

    private fun checkIsValidNameAndType(): Boolean {
        return when {
            binding.nameProduct.text.isNullOrEmpty() -> {
                if (binding.typeProductEditText.text.isNullOrEmpty()) {
                    binding.nameProduct.error = getString(R.string.complete_this_input)
                    false
                } else {
                    true
                }
            }

            binding.typeProductEditText.text.isNullOrEmpty() -> {
                if (binding.nameProduct.text.isNullOrEmpty()) {
                    binding.typeProductEditText.error = getString(R.string.complete_this_input)
                    false
                } else {
                    true
                }
            }

            else -> true
        }
    }

    private fun saveProductInFB(
        nameProduct: String,
        value: Int,
        typeProduct: String = "",
        measure: String,
        isFavorite: Boolean
    ) {
        productViewModel.saveNewProductInFirebase(
            name = nameProduct,
            typeProduct = typeProduct,
            costItem = value.toDouble(),
            isFavorite = isFavorite,
            measure = measure,
        )
    }

    private fun clearAndResetForm(editTexts: List<EditText>) {
        editTexts.forEach { editText ->
            editText.text?.clear()
        }
        binding.recyclerViewOrders.smoothScrollToPosition(buyAdapter.itemCount)
        binding.nameProduct.requestFocus()
    }

    private fun processNotExistProduct(
        nameProduct: String,
        amount: Int,
        total: Int,
        value: Int
    ) {
        productViewModel.currentProduct = ProductsBuyBinding(
            name = nameProduct,
            amount = amount,
            total = total,
            costItem = value,
        )
    }

    private fun processExistProduct(
        product: ProductsBuyBinding,
        total: Int,
        value: Int,
        amount: Int
    ) {
        productViewModel.currentProduct.apply {
            this.id = product.id
            this.productBuyId = product.productBuyId
            this.total = total
            this.costItem = value
            this.amount = amount
        }
        //saveProductToBuy()
    }

    private fun saveProductToBuy() {
        viewModel.saveProductBuy(productViewModel.currentProduct)
        productViewModel.currentProduct = ProductsBuyBinding()
    }

    private fun calculateAndShowPrice() {

        val count = removeCoinSymbol(binding.amountEditText.text.toString())
        val itemValue = removeCoinSymbol(binding.valueEditText.text.toString())
        binding.totalEditText.text?.clear()
        binding.totalEditText.setText(
            buildCoinFormat(count.toInt() * itemValue.toInt())
        )
    }

    private fun loadFirebaseProducts() {
        productViewModel.loadProducts()
    }

    private fun updateAndShowTotalValue(it: Int) {
        binding.totalBuy.text = buildCoinFormat(it)
        binding.buttonSaveBuy.isVisible = it > 0
    }

}