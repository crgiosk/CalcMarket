package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.calcmarket.core.BaseListAdapter
import com.calcmarket.core.BaseItemCallback
import com.calcmarket.core.BaseViewHolder
import com.calcmarket.core.Extensions
import com.calcmarket.databinding.ItemBuyBinding
import com.calcmarket.ui.binds.BuyBinding

class BuysListAdapter(
    val onClickCategory: (BuyBinding) -> Unit
) : BaseListAdapter<BuyBinding>(
    itemCallback = object : BaseItemCallback<BuyBinding>() {
        override fun areItemsTheSame(oldItem: BuyBinding, newItem: BuyBinding): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BuyBinding, newItem: BuyBinding): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBuyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BuyBinding>, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val myView: ItemBuyBinding) : BaseViewHolder<BuyBinding>(myView.root) {

        override fun bind(model: BuyBinding) {
            myView.nameBuy.text = model.name
            myView.totalBuy.text = Extensions.buildCoinFormat(model.totalBuyValue)
            myView.countItems.text = model.itemsCount.toString()
            myView.root.setOnClickListener { onClickCategory.invoke(model) }
        }
    }
}