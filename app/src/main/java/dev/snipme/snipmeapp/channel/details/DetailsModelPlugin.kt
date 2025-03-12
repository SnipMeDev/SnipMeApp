package dev.snipme.snipmeapp.channel.details

import dev.snipme.snipmeapp.channel.ChannelDetailsModel
import dev.snipme.snipmeapp.channel.FlowChannelEventStreamHandler
import dev.snipme.snipmeapp.channel.FlowChannelStateStreamHandler
import dev.snipme.snipmeapp.channel.ModelPlugin
import dev.snipme.snipmeapp.channel.toModelData
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

    override fun toggleFavorite() {
        model.toggleFavorite()
    }

    override fun saveImage(image: ByteArray) {
        model.save(image)
    }

    override fun copyToClipboard() {
        model.copyToClipboard()
    }

    override fun shareImage(image: ByteArray) {
        model.share(image)
    }

    override fun changeVisibility(isHidden: Boolean) {
        model.changeVisibility(isHidden)
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
            value = (event as? Alert)?.message.orEmpty()
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
            is Deleted -> ChannelDetailsModelEvent.DELETED
            is Alert -> ChannelDetailsModelEvent.ALERT
            else -> ChannelDetailsModelEvent.NONE
        }
}