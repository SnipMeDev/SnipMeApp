package pl.tkadziolka.snipmeandroid.util.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.InputType.TYPE_NULL
import android.util.AttributeSet
import android.util.SparseArray
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.EditorInfo.IME_NULL
import android.widget.EditText
import android.widget.FrameLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import kotlinx.android.synthetic.main.view_outlined_text_input.view.*
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.util.extension.getStyledAttributes
import pl.tkadziolka.snipmeandroid.util.extension.restoreChildViewStates
import pl.tkadziolka.snipmeandroid.util.extension.saveChildViewStates
import timber.log.Timber
import java.util.*
import kotlin.random.Random

private const val SKIP_ANIMATION = true

class OutlinedTextInput : FrameLayout {
    private val editText: EditText? get() = outlinedTextField.editText

    private val randomId get() = Random(Date().time).nextInt(0, 10)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        inflate(context, R.layout.view_outlined_text_input, this)
        editText?.id = randomId
        getStyledAttributes(attrs, R.styleable.OutlinedTextInput).apply {
            try {
                with(outlinedTextField) {
                    hint = getString(R.styleable.OutlinedTextInput_android_hint)
                    editText?.setText(getString(R.styleable.OutlinedTextInput_android_text))
                    editText?.inputType =
                        getInteger(R.styleable.OutlinedTextInput_android_inputType, TYPE_NULL)
                    editText?.imeOptions =
                        getInteger(R.styleable.OutlinedTextInput_android_imeOptions, IME_NULL)
                    editText?.isSingleLine =
                        getBoolean(R.styleable.OutlinedTextInput_android_singleLine, true)

                    val isPassword =
                        getBoolean(R.styleable.OutlinedTextInput_android_password, false)
                    if (isPassword) {
                        endIconMode = END_ICON_PASSWORD_TOGGLE
                        passwordVisibilityToggleRequested(SKIP_ANIMATION)
                        editText?.inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
            } catch (e: Exception) {
                Timber.e("Error during resolving style attributes, error = $e")
            } finally {
                recycle()
            }
        }
    }

    var text: String
        get() = editText?.text?.toString().orEmpty()
        set(value) {
            editText?.setText(value)
        }

    var imeOptions: Int
        get() = editText?.imeOptions ?: IME_NULL
        set(options) {
            editText?.imeOptions = options
        }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
            putSparseParcelableArray(SPARSE_STATE_KEY, saveChildViewStates())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            val childrenState = newState.getSparseParcelableArray<Parcelable>(SPARSE_STATE_KEY)
            childrenState?.let { restoreChildViewStates(it) }
            newState = newState.getParcelable(SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(newState)
    }

    companion object {
        private const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
    }
}