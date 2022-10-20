package pl.tkadziolka.snipmeandroid.util.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import kotlinx.android.synthetic.main.view_snippets_filter.view.*
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.util.extension.drawable
import pl.tkadziolka.snipmeandroid.util.extension.first
import pl.tkadziolka.snipmeandroid.util.extension.setMargins
import pl.tkadziolka.snipmeandroid.util.extension.setOnClick
import pl.tkadziolka.snipmeandroid.util.view.SnippetFilter.*

typealias FilterListener = (SnippetFilter) -> Unit

enum class SnippetFilter { ALL, MINE, SHARED }

class SnippetsFilterView : ConstraintLayout, SavedStateView {
    private var shouldNotify = true

    var selectedFilter = ALL

    var filterListener: FilterListener? = null

    override val onSaveState: (Bundle) -> Bundle = {
        it.apply { putString(SNIPPET_FILTER_KEY, selectedFilter.name) }
    }

    override val onRestoreState: (Bundle) -> Unit = {
        markFilter(valueOf(it.getString(SNIPPET_FILTER_KEY, "")))
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        inflate(context, R.layout.view_snippets_filter, this)
        setupActions()
        markFilter(selectedFilter)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val child = filterViewScrollView.first() as ViewGroup

        if (child.width >= filterViewScrollView.width) {
            val startMargin = resources.getDimensionPixelSize(R.dimen.spacing_big)
            child.first().setMargins(left = startMargin)
        }
    }

    fun setFilter(filter: SnippetFilter) {
        resetSelection()
        selectedFilter = filter
        val view = when (filter) {
            ALL -> viewAllAction
            MINE -> viewMineAction
            SHARED -> viewSharedAction
        }
        view.setTextAppearance(R.style.TextAppearance_Filter_Selected)
        view.background = context.drawable(R.drawable.filter_background_selected)

        if (shouldNotify)
            filterListener?.invoke(filter)
    }

    override fun onSaveInstanceState(): Parcelable? = onSaveInstance(super.onSaveInstanceState())

    override fun onRestoreInstanceState(state: Parcelable?) =
        super.onRestoreInstanceState(onRestoreInstance(state))

    private fun markFilter(filter: SnippetFilter) {
        shouldNotify = false
        setFilter(filter)
        shouldNotify = true
    }

    private fun setupActions() {
        viewAllAction.setOnClick { setFilter(ALL) }
        viewMineAction.setOnClick { setFilter(MINE) }
        viewSharedAction.setOnClick { setFilter(SHARED) }
    }

    private fun resetSelection() {
        filterViewContainer.forEach { view ->
            (view as? TextView)?.let {
                it.setTextAppearance(R.style.TextAppearance_Filter)
                it.background = context.drawable(R.drawable.filter_background)
            }
        }
    }

    private companion object {
        private const val SNIPPET_FILTER_KEY = "snippet_filter_key"
    }
}