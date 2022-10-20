package pl.tkadziolka.snipmeandroid.util.view

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import pl.tkadziolka.snipmeandroid.util.SyntaxHighlighter

class CodeEditText : AppCompatEditText {
    // Default constructor with @JvmOverloads doesn't work
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var previous: String = ""
    private var cursorPosition: Int = 0

    init {
        addTextChangedListener { text: Editable? ->
            if (text.toString() != previous) {
                previous = text.toString()
                cursorPosition = selectionEnd
                setText(SyntaxHighlighter.getHighlighted(text.toString(), ignoreError = true))
                setSelection(cursorPosition)
            }
        }
    }
}