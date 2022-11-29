package pl.tkadziolka.snipmeandroid.domain.snippets

import android.text.SpannableString
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.SnippetResponse
import pl.tkadziolka.snipmeandroid.util.SyntaxHighlighter.getHighlighted
import pl.tkadziolka.snipmeandroid.util.extension.lines
import pl.tkadziolka.snipmeandroid.util.extension.newLineChar
import pl.tkadziolka.snipmeandroid.util.extension.toDate
import pl.tkadziolka.snipmeandroid.util.extension.toSnippetLanguage
import java.util.*

const val PREVIEW_COUNT = 5

class SnippetResponseMapper {

    operator fun invoke(response: SnippetResponse) = with(response) {
        return@with Snippet(
            uuid = id,
            title = title.orEmpty(),
            code = getCode(this),
            language = getLanguage(language),
            visibility = getVisibility(visibility),
            isOwner = is_owner ?: false,
            owner = Owner(owner?.id ?: 0, owner?.username ?: ""),
            modifiedAt = modified_at?.toDate() ?: Date(),
            numberOfLikes = number_of_likes ?: 0,
            numberOfDislikes = number_of_dislikes ?: 0,
            userReaction = getUserReaction(user_reaction)
        )
    }

    private fun getUserReaction(value: String?) =
        when {
            value.equals("like", ignoreCase = true) -> UserReaction.LIKE
            value.equals("dislike", ignoreCase = true) -> UserReaction.DISLIKE
            else -> UserReaction.NONE
        }

    private fun getCode(response: SnippetResponse) = SnippetCode(
        raw = response.code.orEmpty(),
        highlighted = getPreview(response.code.orEmpty())
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