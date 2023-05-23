package com.calcmarket.data.local.di

import android.content.DialogInterface
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.calcmarket.R
import javax.inject.Inject


class DialogConfirm @Inject constructor(private val activity: AppCompatActivity) {

    fun showAlertConfirmationDialog(
        message: String = "",
        title: String = "",
        titlePositiveButton: Int = R.string.title_button_agree,
        titleNegativeButton: Int = R.string.title_button_cancel,
        onAgree: () -> Unit,
        onCancel: (() -> Unit?)? = null,
        isCancelable: Boolean = false
    ) {
        val dialog = AlertDialog.Builder(activity, R.style.DialogTheme)
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