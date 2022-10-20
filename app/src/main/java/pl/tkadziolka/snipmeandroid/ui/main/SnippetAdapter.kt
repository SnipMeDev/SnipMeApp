package pl.tkadziolka.snipmeandroid.ui.main

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_item_snippet.view.*
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.util.extension.inflate
import pl.tkadziolka.snipmeandroid.util.extension.setOnClick
import pl.tkadziolka.snipmeandroid.util.extension.tint

typealias SnippetListener = (Snippet) -> Unit

class SnippetAdapter(
    private val clickListener: SnippetListener,
    private val pressListener: SnippetListener
) : ListAdapter<Snippet, SnippetAdapter.SnippetHolder>(SNIPPET_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnippetHolder =
        SnippetHolder(parent.inflate(R.layout.view_item_snippet), clickListener, pressListener)

    override fun onBindViewHolder(holder: SnippetHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SnippetHolder(
        view: View,
        private val clickListener: SnippetListener,
        private val pressListener: SnippetListener
    ) : RecyclerView.ViewHolder(view) {

        private fun ImageView.accented() = tint(R.color.highlight)

        private fun ImageView.regular() = tint(R.color.white)

        fun bind(snippet: Snippet) = with(itemView) {
            setOnClick { clickListener(snippet) }
            setOnLongClickListener { pressListener(snippet); true }
            snippetTitle.text = snippet.title
            snippetLanguage.text = snippet.language.raw
            with(snippetCode) { text = snippet.code.highlighted }
            bindReactions(snippet)
        }

        private fun View.bindReactions(snippet: Snippet) = with(snippet) {
            val likeDiff = numberOfLikes - numberOfDislikes
            snippetLikeCounter.text = likeDiff.toString()
            when (snippet.userReaction) {
                UserReaction.LIKE -> {
                    snippetLikeIndicator.accented()
                    snippetDislikeIndicator.regular()
                }
                UserReaction.DISLIKE -> {
                    snippetLikeIndicator.regular()
                    snippetDislikeIndicator.accented()
                }
                UserReaction.NONE -> {
                    snippetLikeIndicator.regular()
                    snippetDislikeIndicator.regular()
                }
            }
        }
    }
}

private val SNIPPET_DIFF = object : DiffUtil.ItemCallback<Snippet>() {

    override fun areItemsTheSame(oldItem: Snippet, newItem: Snippet): Boolean =
        oldItem.uuid == newItem.uuid

    override fun areContentsTheSame(oldItem: Snippet, newItem: Snippet): Boolean =
        oldItem == newItem
}