package com.calcmarket.ui.binds

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class CustomAutoComplete(context: Context, attrs: AttributeSet) : AppCompatAutoCompleteTextView(context, attrs) {

    override fun convertSelectionToString(selectedItem: Any): CharSequence {
        return ""
    }
}