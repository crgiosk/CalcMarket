package com.calcmarket.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.calcmarket.databinding.LayoutAutoCompleteProductBinding
import java.util.Locale
import kotlin.collections.ArrayList

class ProductAutoCompleteAdapter(
    val clickClosure: () -> Unit
) : BaseAdapter(), Filterable {

    private var items: MutableList<String> = mutableListOf()
    private val filter = AutoCompleteFilter()

    fun updateItems(list: List<String>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutAutoCompleteProductBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        view.nameProduct.text = items[position]
        return view.root
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.count()
    }

    override fun getFilter(): Filter = filter

    private inner class AutoCompleteFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResult = FilterResults()
            if (constraint.isNullOrEmpty()) {
                filterResult.values = items
                filterResult.count = items.count()
            } else {
                val newValues = ArrayList<String>()
                for (item in items) {
                    if (
                        item.lowercase(Locale.ROOT)
                            .contains(constraint.toString().lowercase(Locale.ROOT))
                    ) {
                        newValues.add(item)
                    }

                }

                filterResult.values = newValues
                filterResult.count = newValues.count()
            }
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
            items.clear()
            result?.let {
                items.addAll(it.values as ArrayList<String>)
                if (items.isEmpty()) {
                    clickClosure()
                    notifyDataSetInvalidated()
                } else {
                    notifyDataSetChanged()
                }
            }
        }
    }
}