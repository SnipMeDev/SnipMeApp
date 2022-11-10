package pl.tkadziolka.snipmeandroid.bridge.main

import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import androidx.core.text.getSpans
import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.inject
import pl.tkadziolka.snipmeandroid.bridge.Bridge
import pl.tkadziolka.snipmeandroid.bridge.ModelPlugin
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.snippets.*
import pl.tkadziolka.snipmeandroid.ui.main.*
import pl.tkadziolka.snipmeandroid.util.view.SnippetFilter
import java.util.*

class MainModelPlugin : ModelPlugin<Bridge.MainModelBridge>(), Bridge.MainModelBridge {

    private val model: MainModel by inject()

    override fun onSetup(
        messenger: BinaryMessenger,
        bridge: Bridge.MainModelBridge?
    ) {
        Bridge.MainModelBridge.setup(messenger, bridge)
    }

    override fun getState(): Bridge.MainModelStateData = getData(model.state.value)

    override fun getEvent(): Bridge.MainModelEventData = getEvent(model.event.value)

    override fun initState() {
        model.initState()
    }

    override fun loadNextPage() {
        model.loadNextPage()
    }

    override fun filter(filter: Bridge.SnippetFilter) {
        val type = (filter.type?.name ?: Bridge.SnippetFilterType.ALL.name).uppercase()
        val snippetFilter = SnippetFilter.valueOf(type)
        model.filter(snippetFilter)
    }

    override fun logOut() {
        model.logOut()
    }

    override fun refreshSnippetUpdates() {
        model.refreshSnippetUpdates()
    }

    private fun getData(viewState: MainViewState) = Bridge.MainModelStateData().apply {
        state = viewState.toModelState()
        is_loading = viewState is Loading
        data = (viewState as? Loaded)?.snippets?.toModelData()
    }

    private fun getEvent(viewEvent: MainEvent) = Bridge.MainModelEventData().apply {
        event = viewEvent.toModelEvent()
        message = (viewEvent as? Alert)?.message
    }

    private fun MainEvent.toModelEvent() =
        when (this) {
            is Alert -> Bridge.MainModelEvent.ALERT
            is Logout -> Bridge.MainModelEvent.LOGOUT
            else -> Bridge.MainModelEvent.NONE
        }

    private fun MainViewState.toModelState() =
        when (this) {
            Loading -> Bridge.ModelState.LOADING
            is Loaded -> Bridge.ModelState.LOADED
            is Error -> Bridge.ModelState.ERROR
        }

    private fun List<Snippet>.toModelData() = map {
        Bridge.Snippet().apply {
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

    private fun ForegroundColorSpan.toSyntaxToken(spannable: Spanned) =
        Bridge.SyntaxToken().let {
            it.start = spannable.getSpanStart(this).toLong()
            it.end = spannable.getSpanEnd(this).toLong()
            it.color = foregroundColor.toLong()
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
}
