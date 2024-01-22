package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.calcmarket.core.BaseViewHolder
import com.calcmarket.core.Extensions
import com.calcmarket.databinding.ItemProductBinding
import com.calcmarket.ui.binds.ProductBinding

private object ProductDiffCallback : DiffUtil.ItemCallback<ProductBinding>() {
    override fun areItemsTheSame(oldItem: ProductBinding, newItem: ProductBinding): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductBinding, newItem: ProductBinding): Boolean {
        return oldItem == newItem
    }
}

class BuyAdapter(
    val onUpdateProduct: (ProductBinding) -> Unit,
    val onDeleteProduct: (ProductBinding) -> Unit
) : ListAdapter<ProductBinding, BuyAdapter.ViewHolder>(ProductDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemProductBinding) : BaseViewHolder<ProductBinding>(binding.root) {

        override fun bind(model: ProductBinding) {

            binding.nameProduct.setText(model.name)
            binding.amountEditText.text = model.amount.toString()
            binding.valueEditText.setText(Extensions.buildCoinFormat(model.costItem))
            binding.totalEditText.setText(Extensions.buildCoinFormat(model.total))

            binding.deleteProduct.setOnClickListener {
                removeItemAtPosition(model)
            }

            binding.removeImageButton.setOnClickListener {
                if (model.amount > 1) {
                    val cant = model.amount - 1
                    val newTotal = model.costItem * cant
                    updateItemPosition(model, cant, newTotal)
                }
            }

            binding.addImageButton.setOnClickListener {
                val cant = model.amount + 1
                val newTotal = model.costItem * cant
                updateItemPosition(model, cant, newTotal)
            }
        }
    }

    private fun removeItemAtPosition(model: ProductBinding) {
        onDeleteProduct.invoke(model)
    }

    private fun updateItemPosition(model: ProductBinding, cant: Int, newTotal: Int) {
        val item: ProductBinding = model.copy()
        item.apply {
            amount = cant
            total = newTotal
            onUpdateProduct.invoke(this)
        }
    }

}