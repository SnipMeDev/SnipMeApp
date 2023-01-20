package pl.tkadziolka.snipmeandroid.util.extension

import android.content.res.TypedArray
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import io.github.kbiakov.codeview.CodeView
import kotlinx.android.synthetic.main.fragment_preview.*
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.util.SyntaxWindowTheme
import pl.tkadziolka.snipmeandroid.util.view.DebouncingOnClickListener

fun ViewGroup.first(): View = getChildAt(0)

fun View.setMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    if (layoutParams is MarginLayoutParams) {
        val layoutParams = layoutParams as MarginLayoutParams
        layoutParams.setMargins(left ?: 0, top ?: 0, right ?: 0, bottom ?: 0)
        requestLayout()
    }
}

fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
    val childViewStates = SparseArray<Parcelable>()
    children.forEach { child -> child.saveHierarchyState(childViewStates) }
    return childViewStates
}

fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
    children.forEach { child -> child.restoreHierarchyState(childViewStates) }
}

fun View.getStyledAttributes(attrs: AttributeSet?, res: IntArray): TypedArray =
    context.theme.obtainStyledAttributes(attrs, res, 0, 0)

fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(resource, this, attachToRoot)!!
}

fun View.visible() {
    isVisible = true
}

fun View.invisible() {
    isInvisible = true
}

fun View.gone() {
    isVisible = false
}

fun EditText.getTyped() = text.toString()

fun View.setOnClick(intervalMillis: Long = 300, doClick: (View) -> Unit) =
    setOnClickListener(
        DebouncingOnClickListener(
            intervalMillis = intervalMillis,
            doClick = doClick
        )
    )

fun CodeView.setCodeWithTheme(code: String, language: String?, theme: SyntaxWindowTheme? = null) {
    with(this) {
        if (language != null) setCode(code, language) else setCode(code)
        setOptions(
            getOptions()!!.copy(
                code = code,
                language = language,
                animateOnHighlight = false,
            )
        )
    }
}

fun ImageView.loadWithFallback(image: String) {
    Glide.with(this)
        .load(image)
        .fallback(R.drawable.placeholder)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .into(this)
}

fun ImageView.tint(@ColorRes res: Int) {
    setColorFilter(
        ContextCompat.getColor(context, res),
        android.graphics.PorterDuff.Mode.SRC_IN
    )
}

fun View.setOnOutsideClick(@DimenRes marginRes: Int, action: () -> Unit) {
    val marginPx = resources.getDimensionPixelSize(marginRes)
    setOnTouchListener { view, event ->
        view.performClick()
        return@setOnTouchListener when {
            event.x <= marginPx -> {
                action(); true
            }
            event.y <= marginPx -> {
                action(); true
            }
            view.width - event.x <= marginPx -> {
                action(); true
            }
            view.height - event.y <= marginPx -> {
                action(); true
            }
            else -> false
        }
    }
}