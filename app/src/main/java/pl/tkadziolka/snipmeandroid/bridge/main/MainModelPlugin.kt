package pl.tkadziolka.snipmeandroid.bridge.main

import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import androidx.core.text.getSpans
import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.inject
import pl.tkadziolka.snipmeandroid.bridge.Bridge
import pl.tkadziolka.snipmeandroid.bridge.ModelPlugin
import pl.tkadziolka.snipmeandroid.bridge.toModelData
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

    private fun List<Snippet>.toModelData() = map { it.toModelData() }
}
