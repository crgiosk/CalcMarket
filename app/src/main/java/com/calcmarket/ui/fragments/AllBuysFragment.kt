package com.calcmarket.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.calcmarket.R
import com.calcmarket.databinding.FragmentAllBuysBinding
import com.calcmarket.ui.adapter.BuyListAdapter
import com.calcmarket.viewmodels.BuysViewModel

class AllBuysFragment : Fragment() {

    private lateinit var binding: FragmentAllBuysBinding

    private val buyAdapter: BuyListAdapter by lazy {
        BuyListAdapter()
    }

    private val viewModel: BuysViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBuysBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        binding.newBuy.setOnClickListener {
            findNavController().navigate(R.id.action_allBuysFragment_to_newBuyFragment)
        }
        observeBuy()
    }

    private fun setupUi() {
        binding.buyExpandableList.apply {
            setHasTransientState(true)
            setAdapter(buyAdapter)
            setGroupIndicator(null)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFullBuys()
    }

    private fun observeBuy() {
        viewModel.fullBuysLiveData().observe(viewLifecycleOwner) {
            if (isVisible){
                buyAdapter.replaceAllData(it)
            }
        }
    }

}