package com.calcmarket.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.R
import com.calcmarket.data.local.di.DialogConfirm
import com.calcmarket.databinding.FragmentAllBuysBinding
import com.calcmarket.ui.adapter.BuysAdapter
import com.calcmarket.viewmodels.BuysViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllBuysFragment : Fragment() {

    private lateinit var binding: FragmentAllBuysBinding

    private val buyAdapter: BuysAdapter by lazy {
        BuysAdapter {
            viewModel.buySelectedSet(it)
            findNavController().navigate(R.id.action_allBuysFragment_to_buyDetailFragment)
        }
    }

    private val viewModel: BuysViewModel by activityViewModels()

    @Inject
    lateinit var confirmDialog: DialogConfirm

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
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.newBuy.setOnClickListener {
            viewModel.checkBuyInProgress {
                if (it == -1) {
                    findNavController().navigate(R.id.action_allBuysFragment_to_newBuyFragment)
                } else {
                    confirmDialog.showAlertConfirmationDialog(
                        message = getString(R.string.message_buy_in_progress),
                        titlePositiveButton = R.string.title_button_continue,
                        titleNegativeButton = R.string.title_button_delete,
                        onPositiveButton = {
                            viewModel.getProductsByBuy(it)
                        },
                        onNegativeButton = {
                            //todo: remove all products by last buy
                            findNavController().navigate(R.id.action_allBuysFragment_to_newBuyFragment)
                        }
                    )
                }
            }
        }
    }

    private fun setupUi() {
        binding.buysRecyclerViewView.apply {
            adapter = buyAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFullBuys()
    }

    private fun setupObservers() {
        viewModel.fullBuysLiveData().observe(viewLifecycleOwner) {
            if (isVisible) {
                buyAdapter.updateData(it)
            }
        }
    }

}