package com.calcmarket.core

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.calcmarket.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.properties.Delegates

object Extensions {
    fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun Activity.hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            this.currentFocus?.let {
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    fun buildCoinFormat(number: Int): String {
        val symbol = DecimalFormatSymbols()
        symbol.groupingSeparator = '.'
        symbol.decimalSeparator = ','
        val format = DecimalFormat("$#,###.##", symbol)
        return format.format(number)
    }

    fun removeCoinSymbol(value: String): String =
        value.replace("$", "")
            .replace(".", "")
            .replace(",", "")

    fun <VH : RecyclerView.ViewHolder, T> RecyclerView.Adapter<VH>.basicDiffUtil(
        initialValue: MutableList<T> = mutableListOf(),
        areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
        areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
    ) = Delegates.observable(initialValue) { _, old, new ->

        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = new.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(old[oldItemPosition], old[newItemPosition])

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areContentsTheSame(old[oldItemPosition], old[newItemPosition])

        })
    }

    fun AppCompatActivity.showAlertConfirmationDialog(
        message: String = "",
        title: String = "",
        titlePositiveButton: Int = R.string.title_button_agree,
        titleNegativeButton: Int = R.string.title_button_cancel,
        onAgree: () -> Unit,
        onCancel: (() -> Unit?)? = null,
        isCancelable: Boolean = false
    ) {
        val dialog = AlertDialog.Builder(this, R.style.DialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(isCancelable)
            .setPositiveButton(titlePositiveButton) { dialog, _ ->
                dialog.dismiss()
                onAgree()
            }
            .setNegativeButton(titleNegativeButton) { dialog, _ ->
                dialog.dismiss()
                onCancel?.invoke()
            }
            .create()

        dialog.show()

        val messageView = dialog.findViewById<TextView>(android.R.id.message)
        messageView?.gravity = Gravity.CENTER

        val titleView = dialog.findViewById<TextView>(androidx.appcompat.R.id.alertTitle)
        titleView?.textSize = 18F

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#7CB230"))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#EB0240"))
    }

}