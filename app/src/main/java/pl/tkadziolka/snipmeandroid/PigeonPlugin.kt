package pl.tkadziolka.snipmeandroid

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.text.getSpans
import io.flutter.embedding.engine.plugins.FlutterPlugin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.tkadziolka.snipmeandroid.bridge.MainModel
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetCode
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetLanguage
import pl.tkadziolka.snipmeandroid.ui.main.*
import pl.tkadziolka.snipmeandroid.util.view.SnippetFilter
import timber.log.Timber

// flutter pub run pigeon \
//  --input pigeons/messages.dart \
//  --dart_out lib/messages.dart \
//  --java_out ../app/src/main/java/pl/tkadziolka/snipmeandroid/Messages.java \
//  --java_package "pl.tkadziolka.snipmeandroid"

@ExperimentalCoroutinesApi
class PigeonPlugin : FlutterPlugin, Messages.MainModelApi, KoinComponent {

    private val mainModel: MainModel by inject()

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        Messages.MainModelApi.setup(binding.binaryMessenger, this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        Messages.MainModelApi.setup(binding.binaryMessenger, null)
    }

    override fun getState(): Messages.MainModelStateData = getData(mainModel.state.value)

    override fun getEvent(): Messages.MainModelEventData = getEvent(mainModel.event.value)

    override fun initState() {
        mainModel.initState()
    }

    override fun loadNextPage() {
        mainModel.loadNextPage()
    }

    override fun filter(filter: Messages.SnippetFilter) {
        val type = (filter.type?.name ?: Messages.SnippetFilterType.ALL.name).uppercase()
        val snippetFilter = SnippetFilter.valueOf(type)
        mainModel.filter(snippetFilter)
    }

    override fun logOut() {
        mainModel.logOut()
    }

    override fun refreshSnippetUpdates() {
        mainModel.refreshSnippetUpdates()
    }

    private fun getData(viewState: MainViewState) = Messages.MainModelStateData().apply {
        state = viewState.toModelState()
        is_loading = viewState is Loading
        data = (viewState as? Loaded)?.snippets?.toModelData()
    }

    private fun getEvent(viewEvent: MainEvent) = Messages.MainModelEventData().apply {
        event = viewEvent.toModelEvent()
        message = (viewEvent as? Alert)?.message
    }

    private fun MainEvent.toModelEvent() =
        when (this) {
            is Alert -> Messages.MainModelEvent.ALERT
            is Logout -> Messages.MainModelEvent.LOGOUT
            else -> Messages.MainModelEvent.NONE
        }

    private fun MainViewState.toModelState() =
        when (this) {
            Loading -> Messages.ModelState.LOADING
            is Loaded -> Messages.ModelState.LOADED
            is Error -> Messages.ModelState.ERROR
        }

    private fun List<Snippet>.toModelData() = map {
        Messages.Snippet().apply {
            uuid = it.uuid
            title = it.title
            code = it.code.toModelSnippetCode()
            language = it.language.toModelSnippetLanguage()
        }
    }

    private fun SnippetCode.toModelSnippetCode() =
        Messages.SnippetCode().let {
            it.raw = raw
            // TODO Debug resolving spans (lines = 5)???
            it.tokens = highlighted.getSpans<ForegroundColorSpan>().map { span ->
                span.toSyntaxToken(highlighted)
            }
            it
        }

    private fun SnippetLanguage.toModelSnippetLanguage() =
        Messages.SnippetLanguage().let {
            it.raw = raw
            it.type = Messages.SnippetLanguageType.valueOf(type.name)
            it
        }

    private fun ForegroundColorSpan.toSyntaxToken(spannable: Spanned) =
        Messages.SyntaxToken().let {
            it.start = spannable.getSpanStart(this).toLong()
            it.end = spannable.getSpanEnd(this).toLong()
            it.color = foregroundColor.toLong()
            it
        }
}




