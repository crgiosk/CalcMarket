package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.calcmarket.core.Extensions
import com.calcmarket.databinding.ItemChildProductBinding
import com.calcmarket.databinding.ItemParentProductBinding
import com.calcmarket.ui.binds.BuyBinding
import com.calcmarket.ui.binds.ProductBinding

class BuyListAdapter: BaseExpandableListAdapter() {

    private val items: MutableList<BuyBinding> = mutableListOf()

    fun replaceAllData(items: List<BuyBinding>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = false

    override fun hasStableIds(): Boolean = false

    override fun getGroup(groupPosition: Int): BuyBinding = items[groupPosition]

    override fun getGroupId(groupPosition: Int): Long = items[groupPosition].id.toLong()

    override fun getGroupCount(): Int = items.count()

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val buy = getGroup(groupPosition)
        val view = ItemParentProductBinding.inflate(LayoutInflater.from(parent?.context))

        view.dateTextView.text = buy.name
        view.totalBuyTextView.text = Extensions.buildCoinFormat(buy.totalBuyValue)
        view.amount.text = buy.items.count().toString()

        return view.root
    }



    override fun getChildrenCount(groupPosition: Int): Int = getGroup(groupPosition).items.count()

    override fun getChild(groupPosition: Int, childPosition: Int): ProductBinding =
        getGroup(groupPosition).items[childPosition]

    override fun getChildId(groupPosition: Int, childPosition: Int): Long =
        getGroup(groupPosition).items[childPosition].id.toLong()

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        val product = getChild(groupPosition, childPosition)
        val view = ItemChildProductBinding.inflate(LayoutInflater.from(parent?.context))
        view.countProduct.text = product.amount.toString()
        view.nameProduct.text = product.name
        view.textViewValueUnit.text = Extensions.buildCoinFormat(product.costItem)
        view.textViewValueTotal.text = Extensions.buildCoinFormat(product.total)

        return view.root
    }



}