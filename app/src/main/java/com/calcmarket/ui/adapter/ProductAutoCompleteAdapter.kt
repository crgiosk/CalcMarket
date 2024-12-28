package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.calcmarket.databinding.LayoutAutoCompleteProductBinding
import com.calcmarket.ui.binds.ProductBinding
import kotlin.collections.ArrayList

class ProductAutoCompleteAdapter(
    private val clickClosure: () -> Unit
) : BaseAdapter(), Filterable {

    private var items: MutableList<ProductBinding> = mutableListOf()
    private var filteredItems: MutableList<ProductBinding> = mutableListOf()
    private val mFilter = AutoCompleteFilter()

    fun updateItems(list: List<ProductBinding>) {
        items.clear()
        items.addAll(list)
        filteredItems.clear()
        filteredItems.addAll(list)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val view = LayoutAutoCompleteProductBinding.inflate(layoutInflater, parent, false)
        val item = filteredItems[position]

        val nameProduct = when {
            item.type.isNotEmpty() && item.name.isEmpty() -> item.type
            item.name.isNotEmpty() && item.type.isEmpty() -> item.name
            else -> "${item.type} - ${item.name}"
        }
        view.nameProduct.text = nameProduct
        return view.root
    }

    override fun getItem(position: Int): Any {
        return filteredItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return filteredItems.count()
    }

    override fun getFilter(): Filter = mFilter

    private inner class AutoCompleteFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint.isNullOrEmpty()) {
                filterResults.values = items
                filterResults.count = items.count()
            } else {
                val query = constraint.toString().lowercase()
                val matchedValues = items.filter { item ->
                    val isValidName = item.name.contains(query, ignoreCase = true)
                    val isValidType = item.type.contains(query, ignoreCase = true)
                    isValidName || isValidType
                }

                filterResults.values = matchedValues
                filterResults.count = matchedValues.count()
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
            result?.values?.let {
                filteredItems = it as ArrayList<ProductBinding>
                if (filteredItems.isEmpty()) {
                    clickClosure()
                    notifyDataSetInvalidated()
                } else {
                    notifyDataSetChanged()
                }
            }
        }
    }
}