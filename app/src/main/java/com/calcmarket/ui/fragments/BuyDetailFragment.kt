package com.calcmarket.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.databinding.FragmentBuyDetailBinding
import com.calcmarket.ui.adapter.ProductsByBuyAdapter
import com.calcmarket.viewmodels.BuysViewModel

class BuyDetailFragment : Fragment() {

    private val binding: FragmentBuyDetailBinding by lazy {
        FragmentBuyDetailBinding.inflate(layoutInflater, null, false)
    }

    private val viewModel: BuysViewModel by activityViewModels()

    private val myAdapter: ProductsByBuyAdapter by lazy {
        ProductsByBuyAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
        viewModel.getProductsByBuy(viewModel.buySelectedLiveData.value?.id ?: 0)
    }

    private fun setupObservers() {
        viewModel.productsByBuyLiveData().observe(viewLifecycleOwner) {
            myAdapter.updateData(it)
        }
    }

    private fun setupUI() {
        binding.productsRecyclerViewView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

}