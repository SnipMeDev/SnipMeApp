package dev.snipme.snipmeapp.domain.snippets

import android.text.SpannableString
import java.util.Date

data class Snippet(
    val uuid: String,
    val title: String,
    val code: SnippetCode,
    val language: SnippetLanguage,
    val visibility: SnippetVisibility,
    val modifiedAt: Date,
    val favorite: Boolean,
) {
    companion object {
        val EMPTY = Snippet(
            uuid = "",
            title = "",
            code = SnippetCode("", SpannableString("")),
            language = SnippetLanguage("", SnippetLanguageType.UNKNOWN),
            visibility = SnippetVisibility.HIDDEN,
            modifiedAt = Date(),
            favorite = false
        )
    }
}

data class Owner(val id: Int, val login: String)

data class SnippetCode(val raw: String, val highlighted: SpannableString)

data class SnippetLanguage(val raw: String, val type: SnippetLanguageType)
