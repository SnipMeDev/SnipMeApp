package dev.snipme.snipmeapp.domain.snippets

import android.text.SpannableString
import dev.snipme.snipmeapp.domain.reaction.UserReaction
import java.util.*

data class Snippet(
    val uuid: String,
    val title: String,
    val code: SnippetCode,
    val language: SnippetLanguage,
    val visibility: SnippetVisibility,
    val isOwner: Boolean,
    val owner: Owner,
    val modifiedAt: Date,
    val numberOfLikes: Int,
    val numberOfDislikes: Int,
    val userReaction: UserReaction
) {
    companion object {
        val EMPTY = Snippet(
            uuid = "",
            title = "",
            code = SnippetCode("", SpannableString("")),
            language = SnippetLanguage("", SnippetLanguageType.UNKNOWN),
            visibility = SnippetVisibility.PRIVATE,
            isOwner = false,
            owner = Owner(0, ""),
            modifiedAt = Date(),
            numberOfLikes = 0,
            numberOfDislikes = 0,
            userReaction = UserReaction.NONE
        )
    }
}

data class Owner(val id: Int, val login: String)

data class SnippetCode(val raw: String, val highlighted: SpannableString)

data class SnippetLanguage(val raw: String, val type: SnippetLanguageType)
