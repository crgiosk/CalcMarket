package com.calcmarket.core

import androidx.recyclerview.widget.DiffUtil

abstract class BaseItemCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    abstract override fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    abstract override fun areContentsTheSame(oldItem: T, newItem: T): Boolean
}