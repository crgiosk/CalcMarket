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
import androidx.lifecycle.lifecycleScope
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
import com.calcmarket.ui.binds.ProductBinding
import com.calcmarket.ui.binds.ProductsBuyBinding
import com.calcmarket.viewmodels.NewBuyViewModel
import com.calcmarket.viewmodels.ProductViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewBuyFragment : Fragment() {

    @Inject
    lateinit var confirmDialog: DialogConfirm

    private val firebase = FirebaseCrashlytics.getInstance()

    private val newBuyViewModel: NewBuyViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()

    private lateinit var binding: FragmentNewBuyBinding
    private val buyAdapter: BuyAdapter by lazy {
        BuyAdapter(
            onUpdateProduct = {
                newBuyViewModel.updateItemBuy(it)
            },
            onDeleteProduct = {
                newBuyViewModel.deleteItemBuy(it)
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
        newBuyViewModel.checkBuyInProgress {
            if (it != null) {
                newBuyViewModel.buySelectedSet(it)
            } else {
                newBuyViewModel.createNewBuy()
            }
        }
    }

    private fun setupUI() {
        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, true)
            adapter = buyAdapter
        }

        binding.nameProduct.setAdapter(autoCompleteAdapter)
        newBuyViewModel.currentProductSelected = ProductsBuyBinding()
    }

    private fun setDefaultFocus() {
        binding.nameProduct.requestFocus()
        binding.nameProduct.showKeyboard()
    }

    private fun setupObservers() {
        productViewModel.productsToShowBySearch().observe(viewLifecycleOwner) {
            if (isVisible) autoCompleteAdapter.updateItems(it)
        }

        newBuyViewModel.productsByBuyLiveData.observe(viewLifecycleOwner) { products ->
            buyAdapter.submitList(products)
            val totalValue = products.sumOf { it.total }
            updateAndShowTotalValue(totalValue)
            updateDataBuy()
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
                    updateDataBuy()
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
                if (p0?.isNotEmpty() == true) {
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
            binding.typeProductEditText.setText(itemSelected.type)
            binding.measureProductEditText.setText(itemSelected.measure)
            binding.valueEditText.requestFocus()

            newBuyViewModel.currentProductSelected = itemSelected.toProductsBuyBinding()
            binding.valueEditText.setText(
                buildCoinFormat(itemSelected.costItem)
            )
        }
    }

    private fun addProduct() {
        if (checkIsValidNameAndType() && checkIsValidOthersInputs()) {
            val productsBuyBinding = createProductFromForm()

            productViewModel.getLocalProductByName(productsBuyBinding.name) { product ->
                if (product != null) {

                    newBuyViewModel.updateProductSelectedData(
                        product,
                        productsBuyBinding.total,
                        productsBuyBinding.costItem,
                        productsBuyBinding.amount
                    )
                    newBuyViewModel.saveProductBuy(newBuyViewModel.currentProductSelected){
                        clearAndResetForm()
                    }
                    sendTagsToFirebase(productsBuyBinding)
                } else {
                    //showLoading
                    saveProductInFB(productsBuyBinding)
                }
            }
        }
    }

    private fun sendTagsToFirebase(productsBuyBinding: ProductsBuyBinding) {
        firebase.setCustomKey("name_product", productsBuyBinding.name)
        firebase.setCustomKey("cost_product", productsBuyBinding.costItem)
        firebase.setCustomKey("amount_product", productsBuyBinding.amount)
        firebase.sendUnsentReports()
    }

    private fun createProductFromForm(): ProductsBuyBinding {
        val total = removeCoinSymbol(binding.totalEditText.text.toString()).toInt()
        val value = removeCoinSymbol(binding.valueEditText.text.toString()).toInt()
        val nameProduct = binding.nameProduct.text?.toString() ?: String()
        val amount = binding.amountEditText.text?.toString()?.toInt() ?: 0
        val type = binding.typeProductEditText.text?.toString() ?: ""
        val measure = binding.measureProductEditText.text?.toString() ?: ""

        return ProductsBuyBinding(
            name = nameProduct,
            costItem = value,
            total = total,
            amount = amount,
            measure = measure,
            type = type
        )

    }

    private fun checkIsValidOthersInputs(): Boolean {
        val valueProduct = removeCoinSymbol(
            binding.valueEditText.text.toString()
        ).toIntOrNull() ?: 0

        return when {
            valueProduct <= 0 -> {
                binding.valueEditText.error = getString(R.string.complete_this_input)
                false
            }

            (binding.measureProductEditText.text?.toString() ?: "").isEmpty() -> {
                binding.measureProductEditText.error = getString(R.string.complete_this_input)
                false
            }

            (binding.amountEditText.text?.toString()?.toIntOrNull() ?: 0) <= 0 -> {
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

    private fun saveProductInFB(product: ProductsBuyBinding) {

        productViewModel.saveNewProductInFirebase(
            name = product.name,
            typeProduct = product.type,
            costItem = product.costItem.toDouble(),
            isFavorite = product.isFavorite,
            measure = product.measure,
            onComplete = {

                productViewModel.getLocalProductByName(product.name) { productSaved ->
                    if (productSaved != null) {
                        newBuyViewModel.updateProductSelectedData(
                            product = productSaved,
                            total = product.total,
                            value = product.costItem,
                            amount = product.amount
                        )
                        newBuyViewModel.saveProductBuy(newBuyViewModel.currentProductSelected) {
                            clearAndResetForm()
                        }
                    }
                }
            }
        )
    }

    private fun updateDataBuy() {

        newBuyViewModel.buySelectedLiveData.value?.let { buy ->
            newBuyViewModel.updateItemsBuy(buy.id, buyAdapter.currentList)
        }
    }

    private fun clearAndResetForm() {
        lifecycleScope.launch(Dispatchers.Main) {
            val editTexts = binding.formInputs.touchables.filterIsInstance<EditText>()
            editTexts.forEach { editText ->
                editText.text?.clear()
            }
            binding.recyclerViewOrders.smoothScrollToPosition(buyAdapter.itemCount)
            binding.nameProduct.requestFocus()
        }
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