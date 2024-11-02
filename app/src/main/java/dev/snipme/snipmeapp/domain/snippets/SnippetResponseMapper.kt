package dev.snipme.snipmeapp.domain.snippets

import android.text.SpannableString
import dev.snipme.snipmeapp.domain.reaction.UserReaction
import dev.snipme.snipmeapp.infrastructure.local.SnippetWithOwner
import dev.snipme.snipmeapp.util.SyntaxHighlighter.getHighlighted
import dev.snipme.snipmeapp.util.extension.lines
import dev.snipme.snipmeapp.util.extension.newLineChar
import dev.snipme.snipmeapp.util.extension.toDate
import dev.snipme.snipmeapp.util.extension.toSnippetLanguage
import java.util.*

const val PREVIEW_COUNT = 5

class SnippetResponseMapper {

    operator fun invoke(response: SnippetWithOwner) = with(response.snippet) {
        return@with Snippet(
            uuid = id.toString(),
            title = title,
            code = getCode(code),
            language = getLanguage(language),
            visibility = getVisibility(visibility),
            isOwner = response.isOwner,
            owner = Owner(ownerId , response.ownerName),
            modifiedAt = modifiedAt.toDate(),
            numberOfLikes = numberOfLikes,
            numberOfDislikes = numberOfDislikes,
            userReaction = getUserReaction(userReaction)
        )
    }

    private fun getUserReaction(value: String?) =
        when {
            value.equals("like", ignoreCase = true) -> UserReaction.LIKE
            value.equals("dislike", ignoreCase = true) -> UserReaction.DISLIKE
            else -> UserReaction.NONE
        }

    private fun getCode(code: String) = SnippetCode(
        raw = code.orEmpty(),
        highlighted = getPreview(code)
    )

    private fun getLanguage(language: String?) = SnippetLanguage(
        raw = language.orEmpty(),
        type = language.toSnippetLanguage()
    )

    private fun getPreview(code: String): SpannableString {
        val preview = code.lines(PREVIEW_COUNT).joinToString(separator = newLineChar)
        return getHighlighted(preview)
    }

    private fun getVisibility(visibility: String?): SnippetVisibility {
        if (visibility == null) return SnippetVisibility.PRIVATE
        return SnippetVisibility.valueOf(visibility)
    }
}