package com.calcmarket.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calcmarket.R
import com.calcmarket.viewmodels.AllBuysViewModel

class AllBuysFragment : Fragment() {

    companion object {
        fun newInstance() = AllBuysFragment()
    }

    private val viewModel: AllBuysViewModel by lazy {
        ViewModelProvider(this).get(AllBuysViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_buys, container, false)
    }

}