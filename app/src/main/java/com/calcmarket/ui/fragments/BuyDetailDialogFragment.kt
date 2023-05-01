package com.calcmarket.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.calcmarket.databinding.FragmentBuyDetailDialogBinding
import com.calcmarket.viewmodels.BuysViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BuyDetailDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: BuysViewModel by activityViewModels()

    private lateinit var binding: FragmentBuyDetailDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuyDetailDialogBinding.inflate(layoutInflater, null,false)
        return binding.root

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setDimAmount(0.4f) /** Set dim amount here (the dimming factor of the parent fragment) */

            /** IMPORTANT! Here we set transparency to dialog layer */
            setOnShowListener {
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)

            }
        }
    }
/*
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        isCancelable = false
        builder.setView(binding.root)
        binding.root.setOnClickListener {
            requireActivity().finish()
        }
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

*/

}