package com.calcmarket.ui.binds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.core.Extensions
import com.calcmarket.core.Extensions.basicDiffUtil
import com.calcmarket.databinding.ItemBuyBinding

class BuyAdapter(
    val onChangeTotal: (Int) -> Unit
) : RecyclerView.Adapter<BuyAdapter.ViewHolder>() {

    private var items: MutableList<BuyBinding> by basicDiffUtil(
        areContentsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areItemsTheSame = { old, new -> old == new }
    )

    fun addItem(item: BuyBinding) {
        items.add(item)
        onChangeTotal(getTotalBuy())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemBuyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], position)
    }

    override fun getItemCount(): Int = items.count()

    private fun getTotalBuy(): Int = items.sumOf { it.total }

    inner class ViewHolder(private val binding: ItemBuyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(model: BuyBinding, position: Int) {

            binding.nameProduct.setText(model.name)
            binding.amountEditText.text = model.amount.toString()
            binding.valueEditText.setText(Extensions.buildCoinFormat(model.costItem))
            binding.totalEditText.setText(Extensions.buildCoinFormat(model.total))

            binding.deleteProduct.setOnClickListener {
                if (items.removeIf { it.id == model.id }){
                    notifyItemRemoved(position)
                    onChangeTotal(getTotalBuy())
                }
            }

            binding.removeImageButton.setOnClickListener {

                if (model.amount > 1) {

                    val cant = model.amount - 1
                    val newTotal = model.costItem * cant

                    binding.totalEditText.setText(Extensions.buildCoinFormat(newTotal))
                    binding.amountEditText.text = cant.toString()

                    items.find { it.id == model.id }?.let {
                        it.amount = cant
                        it.total = newTotal
                        onChangeTotal(getTotalBuy())
                    }
                }
            }

            binding.addImageButton.setOnClickListener {

                val cant = model.amount + 1
                val newTotal = model.costItem * cant

                binding.totalEditText.setText(Extensions.buildCoinFormat(newTotal))
                binding.amountEditText.text = cant.toString()

                items.find { it.id == model.id }?.let {
                    it.amount = cant
                    it.total = newTotal
                    onChangeTotal(getTotalBuy())
                }
            }


        }

    }
}