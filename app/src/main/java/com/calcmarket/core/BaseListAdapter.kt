package com.calcmarket.core

import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T : Any>(
    itemCallback: BaseItemCallback<T>
) : ListAdapter<T, BaseViewHolder<T>>(itemCallback)