package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.core.BaseViewHolder
import com.calcmarket.core.Extensions
import com.calcmarket.databinding.ItemProductBinding
import com.calcmarket.ui.binds.ProductBinding

class BuyAdapter(
    val onChangeTotal: (Int) -> Unit,
    val onUpdateProduct: (ProductBinding) -> Unit,
    val onDeleteProduct: (ProductBinding) -> Unit
) : RecyclerView.Adapter<BuyAdapter.ViewHolder>() {

    private val items: MutableList<ProductBinding> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemProductBinding) : BaseViewHolder<ProductBinding>(binding.root) {

        override fun bind(model: ProductBinding) {

            binding.nameProduct.setText(model.name)
            binding.amountEditText.text = model.amount.toString()
            binding.valueEditText.setText(Extensions.buildCoinFormat(model.costItem))
            binding.totalEditText.setText(Extensions.buildCoinFormat(model.total))

            binding.deleteProduct.setOnClickListener {
                removeItemAtPosition(adapterPosition)
            }

            binding.removeImageButton.setOnClickListener {

                if (model.amount > 1) {

                    val cant = model.amount - 1
                    val newTotal = model.costItem * cant
                    updateItemPosition(model, cant, newTotal, adapterPosition)
                }
            }

            binding.addImageButton.setOnClickListener {

                val cant = model.amount + 1
                val newTotal = model.costItem * cant

                items.find { it.id == model.id }?.let {
                    it.amount = cant
                    it.total = newTotal
                    updateItemPosition(model, cant, newTotal, adapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.count()

    fun getData() = items

    fun mySubmitList(list: List<ProductBinding>) {
        items.clear()
        items.addAll(list)
        notifyItemRangeChanged(0, list.count())
        notifyChangeTotalBuy()
    }

    private fun removeItemAtPosition(position: Int) {
        onDeleteProduct(items[position])
        items.removeAt(position)
        notifyChangeTotalBuy()
    }

    private fun updateItemPosition(model: ProductBinding, cant: Int, newTotal: Int, position: Int) {
        items.find { it.id == model.id }?.let {
            it.amount = cant
            it.total = newTotal
            onUpdateProduct(it)
            notifyChangeTotalBuy()
        }
    }

    private fun notifyChangeTotalBuy() {
        onChangeTotal(
            items.sumOf { it.total }
        )
    }

}