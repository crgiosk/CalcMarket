package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.core.BaseViewHolder
import com.calcmarket.core.Extensions
import com.calcmarket.databinding.ItemBuyBinding
import com.calcmarket.ui.binds.BuyBinding

class BuysAdapter(
    val onClickCategory: (BuyBinding) -> Unit
) : RecyclerView.Adapter<BuysAdapter.ViewHolder>() {

    private val items = mutableListOf<BuyBinding>()

    fun updateData(data: List<BuyBinding>) {
        items.clear()
        items.addAll(data)
        notifyItemRangeChanged(0, items.count())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBuyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class ViewHolder(private val myView: ItemBuyBinding) : BaseViewHolder<BuyBinding>(myView.root) {

        override fun bind(item: BuyBinding) {
            myView.nameBuy.text = item.name
            myView.totalBuy.text = Extensions.buildCoinFormat(item.totalBuyValue)
            myView.countItems.text = item.itemsCount.toString()
            myView.root.setOnClickListener { onClickCategory.invoke(item) }
        }
    }
}