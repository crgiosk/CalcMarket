package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.core.BaseItemCallback
import com.calcmarket.core.Extensions
import com.calcmarket.core.Extensions.basicDiffUtil
import com.calcmarket.databinding.ItemProductBinding
import com.calcmarket.ui.binds.ProductBinding

class BuyAdapter(
    val onChangeTotal: (Int) -> Unit
) : ListAdapter<ProductBinding,BuyAdapter.ViewHolder>(
    object : BaseItemCallback<ProductBinding>() {
        override fun areItemsTheSame(oldItem: ProductBinding, newItem: ProductBinding): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductBinding, newItem: ProductBinding): Boolean {
            return oldItem == newItem
        }

    }
) {

    private var items: MutableList<ProductBinding> by basicDiffUtil(
        areItemsTheSame = { old, new -> old == new }
    ) { oldItem, newItem -> oldItem.id == newItem.id }

    fun updateData(data: List<ProductBinding>) {
        items.clear()
        items.addAll(data)
        onChangeTotal(getTotalBuy())
    }

    fun getData() = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.count()

    private fun getTotalBuy(): Int = items.sumOf { it.total }

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(model: ProductBinding) {

            binding.nameProduct.setText(model.name)
            binding.amountEditText.text = model.amount.toString()
            binding.valueEditText.setText(Extensions.buildCoinFormat(model.costItem))
            binding.totalEditText.setText(Extensions.buildCoinFormat(model.total))

            binding.deleteProduct.setOnClickListener {
                items.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                onChangeTotal(getTotalBuy())
            }

            binding.removeImageButton.setOnClickListener {

                if (model.amount > 1) {

                    val cant = model.amount - 1
                    val newTotal = model.costItem * cant

                    items.find { it.id == model.id }?.let {
                        it.amount = cant
                        it.total = newTotal
                        onChangeTotal(getTotalBuy())
                        notifyItemChanged(adapterPosition)
                    }
                }
            }

            binding.addImageButton.setOnClickListener {

                val cant = model.amount + 1
                val newTotal = model.costItem * cant

                items.find { it.id == model.id }?.let {
                    it.amount = cant
                    it.total = newTotal
                    onChangeTotal(getTotalBuy())
                    notifyItemChanged(adapterPosition)
                }
            }


        }

    }
}