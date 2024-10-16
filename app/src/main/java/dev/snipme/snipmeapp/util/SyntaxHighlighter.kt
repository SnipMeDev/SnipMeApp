package dev.snipme.snipmeapp.util

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.text.set
import dev.snipme.snipmeapp.BuildConfig
import dev.snipme.snipmeapp.util.SyntaxPhrases.commentTerminators
import dev.snipme.snipmeapp.util.SyntaxPhrases.keywords
import dev.snipme.snipmeapp.util.SyntaxPhrases.multilineCommentTerminators
import dev.snipme.snipmeapp.util.SyntaxPhrases.textTerminators
import dev.snipme.snipmeapp.util.SyntaxPhrases.wordTerminators
import dev.snipme.snipmeapp.util.extension.containsDefault
import dev.snipme.snipmeapp.util.extension.indicesOf
import dev.snipme.snipmeapp.util.extension.lengthToEOF
import dev.snipme.snipmeapp.util.extension.opaque
import timber.log.Timber
import java.util.*

data class SyntaxWindowTheme(
    val number: Int,
    val note: Int,
    val numberBackground: Int,
    val contentBackground: Int
)

data class SyntaxTheme(
    val code: Int = 0x808080,
    val keyword: Int = 0xCC7832,
    val text: Int = 0x6A8759,
    val literal: Int = 0x6897BB, // Number, bool, null etc
    val comment: Int = 0x909090,
    val metadata: Int = 0xBBB529,
    val multilineComment: Int = 0x629755
)

private const val START_INDEX = 0
private const val SINGLE_CHAR = 1
private const val TWO_ELEMENTS = 2

// TODO
//  1. Handle annotations (metadata starts with @)
object SyntaxHighlighter {

    fun getHighlighted(
        code: String,
        theme: SyntaxTheme = SyntaxTheme(),
        ignoreError: Boolean = false
    ): SpannableString =
        try {
            SpannableString(code).apply {
                Timber.d("--- START CODE HIGHLIGHTING ---")
                highlightKeywords(code, theme)
                highlightTexts(code, theme)
                highlightNumbers(code, theme)
                highlightComments(code, theme)
                Timber.d("--- END CODE HIGHLIGHTING ---")
            }
        } catch (e: Exception) {
            Timber.d("--- ERROR CODE HIGHLIGHTING ---")
            Timber.e("Error: $e")
            if (BuildConfig.DEBUG && ignoreError.not()) {
                val errorPhrase = "ERROR = $e \nRAW CODE: \n"
                SpannableString(errorPhrase + code).apply {
                    setSpan(ForegroundColorSpan(Color.RED), START_INDEX, errorPhrase.length, 0)
                }
            } else {
                SpannableString(code)
            }
        }

    private fun Spannable.applyColor(start: Int, end: Int, color: Int) {
        this[start..end] = ForegroundColorSpan(color)
    }

    private fun Spannable.highlightKeywords(code: String, theme: SyntaxTheme) {
        val keywordsMap = mutableMapOf<String, List<Int>>()
        findKeywords(code).forEach { keyword ->
            val indices = code
                .indicesOf(keyword)
                .filter { isIndexOnlyKeyword(code, it) }

            keywordsMap[keyword] = indices
        }

        debugKeywords(keywordsMap)

        for ((word, indices) in keywordsMap) {
            indices.forEach { index ->
                applyColor(start = index, end = index + word.length, color = theme.keyword.opaque)
            }
        }
    }

    private fun Spannable.highlightTexts(code: String, theme: SyntaxTheme) {
        findTextSubstring(code)
            .also { debugTexts(code, it) }
            .forEach { substring ->
                val (start, end) = substring
                applyColor(start, end + SINGLE_CHAR, theme.text.opaque)
            }
    }

    private fun Spannable.highlightNumbers(code: String, theme: SyntaxTheme) {
        findDigitIndices(code)
            .also { debugNumbers(code, it) }
            .forEach { index ->
                applyColor(start = index, end = index + SINGLE_CHAR, color = theme.literal.opaque)
            }
    }

    private fun Spannable.highlightComments(code: String, theme: SyntaxTheme) {
        val hasSingle = commentTerminators.any { code.containsDefault(it) }
        val hasMultiline = multilineCommentTerminators.any {
            code.containsDefault(it.first) && code.containsDefault(it.second)
        }

        if (hasSingle) this.highlightSingleComment(code, theme)
        if (hasMultiline) this.highlightMultiComment(code, theme)
    }

    private fun Spannable.highlightSingleComment(code: String, theme: SyntaxTheme) {
        val indices = mutableListOf<Int>()
        commentTerminators.forEach { terminator ->
            indices.addAll(code.indicesOf(terminator))
        }

        debugComments(code, indices)

        indices.forEach { startIndex ->
            val end = startIndex + code.lengthToEOF(startIndex)
            applyColor(start = startIndex, end = end, color = theme.comment.opaque)
        }
    }

    private fun Spannable.highlightMultiComment(code: String, theme: SyntaxTheme) {
        findCommentSubstring(code)
            .also { debugMultilineComment(code, it) }
            .forEach { commentBlock ->
                val (start, end) = commentBlock
                applyColor(start = start, end = end + SINGLE_CHAR, color = theme.multilineComment.opaque)
            }
    }

    private fun findCommentSubstring(code: String): List<Pair<Int, Int>> {
        val comments = mutableListOf<Pair<Int, Int>>()
        val startIndices = mutableListOf<Int>()
        val endIndices = mutableListOf<Int>()

        multilineCommentTerminators.forEach { commentBlock ->
            val (prefix, postfix) = commentBlock
            startIndices.addAll(code.indicesOf(prefix))
            endIndices.addAll(code.indicesOf(postfix).map { it + (postfix.lastIndex) })
        }

        val endIndex = minOf(startIndices.size, endIndices.size) - 1
        for (i in START_INDEX..endIndex) {
            comments.add(Pair(startIndices[i], endIndices[i]))
        }

        return comments
    }

    private fun findDigitIndices(code: String): List<Int> {
        val indices = mutableListOf<Int>()
        code.forEach { char ->
            if (char.isDigit()) indices.add(code.indexOf(char))
        }
        return indices
    }

    private fun findTextSubstring(code: String): List<Pair<Int, Int>> {
        val texts = mutableListOf<Pair<Int, Int>>()
        val textIndices = mutableListOf<Int>()
        textTerminators.forEach {
            textIndices += code.indicesOf(it)
        }

        for (i in START_INDEX..textIndices.lastIndex step TWO_ELEMENTS) {
            texts.add(Pair(textIndices[i], textIndices[i + 1]))
        }

        return texts
    }

    private fun findKeywords(code: String): List<String> =
        code.split(*wordTerminators, ignoreCase = true) // Split into words
            .filter { it.isNotBlank() } // Remove empty
            .map { it.trim() } // Remove whitespaces from phrase
            .map { it.toLowerCase(Locale.getDefault()) } // Standardize
            .filter { it in keywords } // Get supported

    // Sometimes keyword can be found in the middle of word.
    // This returns information if index points only to the keyword
    private fun isIndexOnlyKeyword(code: String, index: Int): Boolean {
        if (index == START_INDEX) return true
        if (index == code.lastIndex) return true

        val charBefore = code[index - 1].toString()
        return charBefore in wordTerminators || charBefore.first().isLetter().not()
    }

    // TODO Correct ranges and make debug print more safe!

    private fun debugKeywords(keywords: Map<String, List<Int>>) {
        keywords.forEach { entry ->
            Timber.d("KEYWORD = ${entry.key}, AT = ${entry.value}")
        }
    }

    private fun debugTexts(phrase: String, substrings: List<Pair<Int, Int>>) {
        substrings.forEach { range ->
            val start = range.first
            val end = range.second
            Timber.d("TEXT = ${phrase.substring(start..end)}, AT = $start")
        }
    }

    private fun debugNumbers(phrase: String, indices: List<Int>) {
        indices.forEach { index ->
            Timber.d("NUMBER = ${phrase[index]}, AT = $index")
        }
    }

    private fun debugComments(phrase: String, indices: List<Int>) {
        indices.forEach { start ->
            val end = start + phrase.lengthToEOF(start)
            Timber.d("COMMENT = ${phrase.substring(start until end)}, AT = $start")
        }
    }

    private fun debugMultilineComment(phrase: String, substrings: List<Pair<Int, Int>>) {
        substrings.forEach { range ->
            val start = range.first
            val end = range.second
            Timber.d("COMMENT = ${phrase.substring(start..end)}, AT = $start")
        }
    }
}