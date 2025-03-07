package dev.snipme.snipmeapp.domain.snippets

import android.text.SpannableString
import dev.snipme.snipmeapp.infrastructure.local.SnippetEntry
import dev.snipme.snipmeapp.util.SyntaxHighlighter.getHighlighted
import dev.snipme.snipmeapp.util.extension.lines
import dev.snipme.snipmeapp.util.extension.newLineChar
import dev.snipme.snipmeapp.util.extension.toDate
import dev.snipme.snipmeapp.util.extension.toSnippetLanguage

const val PREVIEW_COUNT = 5

class SnippetResponseMapper {

    operator fun invoke(response: SnippetEntry) = with(response) {
        return@with Snippet(
            uuid = id.toString(),
            title = title,
            code = getCode(code),
            language = getLanguage(language),
            visibility = getVisibility(visibility),
            isOwner = true,
            owner = Owner(ownerId , response.ownerName),
            modifiedAt = modifiedAt.toDate(),
            favorite = favorite,
        )
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