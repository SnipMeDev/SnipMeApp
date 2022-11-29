package pl.tkadziolka.snipmeandroid.bridge

import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import androidx.core.text.getSpans
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.KoinComponent
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.snippets.*
import java.util.*

/*
 flutter pub run pigeon \
  --input bridge/main_model.dart \
  --dart_out lib/model/main_model.dart \
  --java_out ../app/src/main/java/pl/tkadziolka/snipmeandroid/bridge/Bridge.java \
  --java_package "pl.tkadziolka.snipmeandroid.bridge"
 */

abstract class ModelPlugin<T> : FlutterPlugin, KoinComponent {

    abstract fun onSetup(messenger: BinaryMessenger, bridge: T?)

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        onSetup(binding.binaryMessenger, this as T)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        onSetup(binding.binaryMessenger, null)
    }
}

fun Snippet.toModelData(): Bridge.Snippet {
    val it = this
    return Bridge.Snippet().apply {
        uuid = it.uuid
        title = it.title
        code = it.code.toModelSnippetCode()
        language = it.language.toModelSnippetLanguage()
        owner = it.owner.toModelOwner()
        isOwner = it.isOwner
        voteResult = (it.numberOfLikes - it.numberOfDislikes).toLong()
        userReaction = it.userReaction.toModelUserReaction()
        isLiked = it.userReaction.toModelReactionState(UserReaction.LIKE)
        isDisliked = it.userReaction.toModelReactionState(UserReaction.DISLIKE)
        isPrivate = it.visibility == SnippetVisibility.PRIVATE
        isSaved = calculateSavedState(it.isOwner, it.visibility)
        timeAgo = DateUtils.getRelativeTimeSpanString(
            it.modifiedAt.time,
            Date().time,
            DateUtils.SECOND_IN_MILLIS
        ).toString()
    }
}

private fun Owner.toModelOwner() =
    Bridge.Owner().let {
        it.id = id.toLong()
        it.login = login
        it
    }

private fun SnippetCode.toModelSnippetCode() =
    Bridge.SnippetCode().let {
        it.raw = raw
        it.tokens = highlighted.getSpans<ForegroundColorSpan>().map { span ->
            span.toSyntaxToken(highlighted)
        }
        it
    }

private fun SnippetLanguage.toModelSnippetLanguage() =
    Bridge.SnippetLanguage().let {
        it.raw = raw
        it.type = Bridge.SnippetLanguageType.valueOf(type.name)
        it
    }

private fun UserReaction.toModelUserReaction(): Bridge.UserReaction =
    when (this) {
        UserReaction.LIKE -> Bridge.UserReaction.LIKE
        UserReaction.DISLIKE -> Bridge.UserReaction.DISLIKE
        else -> Bridge.UserReaction.NONE
    }

private fun UserReaction.toModelReactionState(reaction: UserReaction) =
    if (this == UserReaction.NONE) null else this == reaction

private fun calculateSavedState(
    isOwner: Boolean,
    visibility: SnippetVisibility
): Boolean? = when {
    isOwner && visibility == SnippetVisibility.PUBLIC -> false
    isOwner && visibility == SnippetVisibility.PRIVATE -> true
    else -> null
}

private fun ForegroundColorSpan.toSyntaxToken(spannable: Spanned) =
    Bridge.SyntaxToken().let {
        it.start = spannable.getSpanStart(this).toLong()
        it.end = spannable.getSpanEnd(this).toLong()
        it.color = foregroundColor.toLong()
        it
    }
