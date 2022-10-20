package pl.tkadziolka.snipmeandroid.util.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pl.tkadziolka.snipmeandroid.util.SyntaxWindowTheme

val Activity.screenWidth: Int get() {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

val Context.syntaxWindowTheme get() = SyntaxWindowTheme(
    number = getColorAttr(android.R.attr.textColorPrimary),
    note = getColorAttr(android.R.attr.textColorPrimary),
    numberBackground = getColorAttr(android.R.attr.colorBackgroundFloating),
    contentBackground = getColorAttr(android.R.attr.colorBackgroundFloating)
)

fun Context.drawable(@DrawableRes res: Int) = ContextCompat.getDrawable(this, res)

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.safeOpenWebsite(url: String?) {
    if (url == null) return
    if (!url.startsWith("http://") && !url.startsWith("https://")) return

    val page: Uri = Uri.parse(url)
    val browser = Intent(Intent.ACTION_VIEW, page)
    if (browser.resolveActivity(packageManager) != null) {
        startActivity(browser)
    }
}

fun Fragment.showToast(message: String) {
    requireActivity().showToast(message)
}

fun Context.getColorAttr(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

fun Fragment.back() {
    findNavController().navigateUp()
}

fun DialogFragment.showTransparentBackground() {
    dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
}

// This doesn't work with drawable background with insets
fun DialogFragment.showCancelable(cancelable: Boolean = true) {
    isCancelable = cancelable
    this.dialog?.setCanceledOnTouchOutside(cancelable)
}

fun DialogFragment.showFullscreen() {
    val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
    dialog?.window?.setLayout(matchParent, matchParent)
}