package dev.snipme.snipmeapp.bridge.main

import dev.snipme.snipmeapp.bridge.FlowChannelStateStreamHandler
import dev.snipme.snipmeapp.bridge.ModelPlugin
import dev.snipme.snipmeapp.bridge.toModelData
import dev.snipme.snipmeapp.channel.ChannelMainModel
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetFilters
import io.flutter.plugin.common.BinaryMessenger
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import dev.snipme.snipmeapp.channel.MainModelEvent as ChannelMainModelEvent
import dev.snipme.snipmeapp.channel.MainModelEventData as ChannelMainModelEventData
import dev.snipme.snipmeapp.channel.MainModelStateData as ChannelMainModelStateData
import dev.snipme.snipmeapp.channel.ModelState as ChannelModelState
import dev.snipme.snipmeapp.channel.SnippetFilter as ChannelSnippetFilter

class MainModelPlugin : ModelPlugin<ChannelMainModel>(), ChannelMainModel {
    private val model: MainModel by inject()
    private val channelStateFlow by inject<FlowChannelStateStreamHandler>()
    // TODO Think about deleting compare
    private var oldEvent: MainEvent? = null
    private var oldState: MainViewState? = null

    override fun onSetup(
        messenger: BinaryMessenger,
        channelModel: ChannelMainModel?
    ) {
        ChannelMainModel.setUp(messenger, channelModel)
        channelStateFlow.zip(model.state.map { getState(it) })
    }

    override fun resetEvent() {
        model.event.value = Startup
    }

    override fun initState() {
        model.initState()
    }

    override fun filterLanguage(language: String, isSelected: Boolean) {
        model.filterLanguage(language, isSelected)
    }

    override fun filterScope(scope: String) {
        model.filterScope(scope)
    }

    override fun logOut() {
        model.logOut()
    }

    private fun getState(viewState: MainViewState): ChannelMainModelStateData {
        println("StreamHandlerPlugin getState $viewState")
        return ChannelMainModelStateData(
            state = viewState.toModelState(),
            isLoading = viewState is Loading,
            data = (viewState as? Loaded)?.snippets?.toModelData(),
            filter = (viewState as? Loaded)?.filters?.toModelFilter(),
            oldHash = oldState?.hashCode()?.toLong(),
            newHash = viewState.hashCode().toLong(),
        ).also {
            oldState = viewState
        }
    }

    private fun getEvent(viewEvent: MainEvent): ChannelMainModelEventData {
        return ChannelMainModelEventData(
            event = viewEvent.toModelEvent(),
            message = (viewEvent as? Alert)?.message,
            oldHash = oldEvent?.hashCode()?.toLong(),
            newHash = viewEvent.hashCode().toLong(),
        ).also {
            oldEvent = viewEvent
        }
    }

    private fun MainEvent.toModelEvent() =
        when (this) {
            is Alert -> ChannelMainModelEvent.ALERT
            is Logout -> ChannelMainModelEvent.LOGOUT
            else -> ChannelMainModelEvent.NONE
        }

    private fun MainViewState.toModelState() =
        when (this) {
            Loading -> ChannelModelState.LOADING
            is Loaded -> ChannelModelState.LOADED
            is Error -> ChannelModelState.ERROR
        }

    private fun List<Snippet>.toModelData() = map { it.toModelData() }

    private fun SnippetFilters.toModelFilter(): ChannelSnippetFilter {
        return ChannelSnippetFilter(
            languages = languages,
            selectedLanguages = selectedLanguages,
            scopes = scopes,
            selectedScope = selectedScope,
        )
    }
}
