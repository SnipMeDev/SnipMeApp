package pl.tkadziolka.snipmeandroid.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pl.tkadziolka.snipmeandroid.R

class Dialogs(private val context: Context) {
    val loading by lazy {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.loading)
            .setView(R.layout.view_loading)
            .setCancelable(false)
    }

    val ok by lazy {
        MaterialAlertDialogBuilder(context)
            .setPositiveButton(R.string.okay) { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
    }

    val alreadyRegistered by lazy {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.no_account_title)
            .setMessage(R.string.no_account_message)
            .setCancelable(false)
    }

    val quit by lazy {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.quit_title)
            .setMessage(R.string.quit_message)
            .setCancelable(true)
    }
}