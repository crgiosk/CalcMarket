package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.core.BaseViewHolder
import com.calcmarket.databinding.ItemChildProductBinding
import com.calcmarket.ui.binds.ProductBinding

class ProductsByBuyAdapter : RecyclerView.Adapter<ProductsByBuyAdapter.ViewHolder>() {

    private val items = mutableListOf<ProductBinding>()

    fun updateData(data: List<ProductBinding>) {
        items.clear()
        items.addAll(data)
        notifyItemRangeChanged(0, items.count())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChildProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class ViewHolder(private val myView: ItemChildProductBinding) : BaseViewHolder<ProductBinding>(myView.root) {

        override fun bind(item: ProductBinding) {
            myView.product = item
        }
    }
}