package dev.snipme.snipmeapp.channel

import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import androidx.core.text.getSpans
import dev.snipme.snipmeapp.domain.snippets.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.KoinComponent
import java.util.Date
import dev.snipme.snipmeapp.channel.Owner as ChannelOwner
import dev.snipme.snipmeapp.channel.Snippet as ChannelSnippet
import dev.snipme.snipmeapp.channel.SnippetCode as ChannelSnippetCode
import dev.snipme.snipmeapp.channel.SnippetLanguage as ChannelSnippetLanguage
import dev.snipme.snipmeapp.channel.SnippetLanguageType as ChannelSnippetLanguageType
import dev.snipme.snipmeapp.channel.SyntaxToken as ChannelSyntaxToken

abstract class ModelPlugin<T> : FlutterPlugin, KoinComponent {

    abstract fun onSetup(messenger: BinaryMessenger, channelModel: T?)

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        onSetup(binding.binaryMessenger, this as T)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        onSetup(binding.binaryMessenger, null)
    }
}

fun Snippet.toModelData(): ChannelSnippet =
    ChannelSnippet(
        uuid = uuid,
        title = title,
        code = code.toModelSnippetCode(),
        language = language.toModelSnippetLanguage(),
        isFavorite = favorite,
        isPrivate = visibility == SnippetVisibility.PRIVATE,
        timeAgo = DateUtils.getRelativeTimeSpanString(
            modifiedAt.time,
            Date().time,
            DateUtils.SECOND_IN_MILLIS
        ).toString()
    )

private fun Owner.toModelOwner() = ChannelOwner(id = id.toLong(), login = login)

private fun SnippetCode.toModelSnippetCode() =
    ChannelSnippetCode(
        raw = raw,
        tokens = highlighted.getSpans<ForegroundColorSpan>().map { span ->
            span.toSyntaxToken(highlighted)
        },
    )

private fun SnippetLanguage.toModelSnippetLanguage() =
    ChannelSnippetLanguage(
        raw = raw,
        type = ChannelSnippetLanguageType.valueOf(type.name),
    )

private fun ForegroundColorSpan.toSyntaxToken(spannable: Spanned) =
    ChannelSyntaxToken(
        start = spannable.getSpanStart(this).toLong(),
        end = spannable.getSpanEnd(this).toLong(),
        color = foregroundColor.toLong(),
    )
