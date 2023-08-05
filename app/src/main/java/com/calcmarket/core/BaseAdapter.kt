package com.calcmarket.core

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any>(
    itemCallback: BaseItemCallback<T>
) : ListAdapter<T, BaseAdapter.BaseViewHolder<T>>(itemCallback) {

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }
}