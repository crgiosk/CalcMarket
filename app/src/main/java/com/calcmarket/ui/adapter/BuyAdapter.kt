package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.core.Extensions
import com.calcmarket.core.Extensions.basicDiffUtil
import com.calcmarket.databinding.ItemProductBinding
import com.calcmarket.ui.binds.ProductBinding

class BuyAdapter(
    val onChangeTotal: (Int) -> Unit
) : RecyclerView.Adapter<BuyAdapter.ViewHolder>() {

    private var items: MutableList<ProductBinding> by basicDiffUtil(
        areContentsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areItemsTheSame = { old, new -> old == new }
    )

    fun addItem(item: ProductBinding) {
        items.add(item)
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