package pl.tkadziolka.snipmeandroid.util.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import pl.tkadziolka.snipmeandroid.util.view.DebouncingOnClickListener

fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(resource, this, attachToRoot)!!
}

fun View.setOnClick(intervalMillis: Long = 300, doClick: (View) -> Unit) =
    setOnClickListener(
        DebouncingOnClickListener(
            intervalMillis = intervalMillis,
            doClick = doClick
        )
    )

fun ImageView.tint(@ColorRes res: Int) {
    setColorFilter(
        ContextCompat.getColor(context, res),
        android.graphics.PorterDuff.Mode.SRC_IN
    )
}