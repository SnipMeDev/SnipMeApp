package dev.snipme.snipmeapp.bridge.details

import dev.snipme.snipmeapp.bridge.FlowChannelEventStreamHandler
import dev.snipme.snipmeapp.bridge.FlowChannelStateStreamHandler
import dev.snipme.snipmeapp.bridge.ModelPlugin
import dev.snipme.snipmeapp.bridge.toModelData
import dev.snipme.snipmeapp.channel.ChannelDetailsModel
import io.flutter.plugin.common.BinaryMessenger
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import dev.snipme.snipmeapp.channel.DetailsModelEvent as ChannelDetailsModelEvent
import dev.snipme.snipmeapp.channel.DetailsModelEventData as ChannelDetailsModelEventData
import dev.snipme.snipmeapp.channel.DetailsModelStateData as ChannelDetailsModelStateData
import dev.snipme.snipmeapp.channel.ModelState as ChannelModelState

class DetailsModelPlugin : ModelPlugin<ChannelDetailsModel>(), ChannelDetailsModel {
    private val model: DetailsModel by inject()
    private val channelStateFlow by inject<FlowChannelStateStreamHandler>()
    private val channelEventFlow by inject<FlowChannelEventStreamHandler>()

    override fun onSetup(messenger: BinaryMessenger, channelModel: ChannelDetailsModel?) {
        ChannelDetailsModel.setUp(messenger, channelModel)
        channelStateFlow.zip(model.state.map { getModelState(it) })
        channelEventFlow.zip(model.event.map { getModelEvent(it) })
    }

    override fun resetEvent() {
        model.event.value = Idle
    }

    override fun load(uuid: String) {
        model.load(uuid)
    }

    override fun like() {
        model.like()
    }

    override fun dislike() {
        model.dislike()
    }

    override fun save() {
        model.save()
    }

    override fun copyToClipboard() {
        model.copyToClipboard()
    }

    override fun share() {
        model.share()
    }

    override fun delete() {
        model.delete()
    }

    private fun getModelState(viewState: DetailsViewState): ChannelDetailsModelStateData {
        return ChannelDetailsModelStateData(
            state = viewState.toModelState(),
            isLoading = viewState is Loading,
            data = (viewState as? Loaded)?.snippet?.toModelData(),
        )
    }

    private fun getModelEvent(event: DetailsEvent): ChannelDetailsModelEventData {
        return ChannelDetailsModelEventData(
            event = event.toModelEvent(),
            value = (event as? Saved)?.snippetId.toString(),
        )
    }

    private fun DetailsViewState.toModelState() =
        when (this) {
            Loading -> ChannelModelState.LOADING
            is Loaded -> ChannelModelState.LOADED
            else -> ChannelModelState.ERROR
        }

    private fun DetailsEvent.toModelEvent() =
        when (this) {
            is Saved -> ChannelDetailsModelEvent.SAVED
            is Deleted -> ChannelDetailsModelEvent.DELETED
            else -> ChannelDetailsModelEvent.NONE
        }
}