package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.calcmarket.core.BaseViewHolder
import com.calcmarket.core.Extensions
import com.calcmarket.databinding.ItemProductBinding
import com.calcmarket.ui.binds.ProductsBuyBinding

private object ProductDiffCallback : DiffUtil.ItemCallback<ProductsBuyBinding>() {
    override fun areItemsTheSame(oldItem: ProductsBuyBinding, newItem: ProductsBuyBinding): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductsBuyBinding, newItem: ProductsBuyBinding): Boolean {
        return oldItem == newItem
    }
}

class BuyAdapter(
    val onUpdateProduct: (ProductsBuyBinding) -> Unit,
    val onDeleteProduct: (ProductsBuyBinding) -> Unit
) : ListAdapter<ProductsBuyBinding, BuyAdapter.ViewHolder>(ProductDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemProductBinding) : BaseViewHolder<ProductsBuyBinding>(binding.root) {

        override fun bind(model: ProductsBuyBinding) {

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

    private fun removeItemAtPosition(model: ProductsBuyBinding) {
        onDeleteProduct.invoke(model)
    }

    private fun updateItemPosition(model: ProductsBuyBinding, cant: Int, newTotal: Int) {
        val item: ProductsBuyBinding = model.copy()
        item.apply {
            amount = cant
            total = newTotal
            onUpdateProduct.invoke(this)
        }
    }

}