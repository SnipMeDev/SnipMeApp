package dev.snipme.snipmeapp.bridge.Details

import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.inject
import dev.snipme.snipmeapp.bridge.ModelPlugin
import dev.snipme.snipmeapp.bridge.toModelData
import dev.snipme.snipmeapp.channel.ChannelDetailsModel
import dev.snipme.snipmeapp.channel.ModelState as ChannelModelState
import dev.snipme.snipmeapp.channel.DetailsModelStateData as ChannelDetailsModelStateData
import dev.snipme.snipmeapp.channel.DetailsModelEvent as ChannelDetailsModelEvent
import dev.snipme.snipmeapp.channel.DetailsModelEventData as ChannelDetailsModelEventData

class DetailsModelPlugin : ModelPlugin<ChannelDetailsModel>(), ChannelDetailsModel {
    private val model: DetailsModel by inject()
    private var oldEvent: DetailsEvent? = null
    private var oldState: DetailsViewState? = null

    override fun resetEvent() {
        model.event.value = Idle
    }

    override fun onSetup(messenger: BinaryMessenger, channelModel: ChannelDetailsModel?) {
        ChannelDetailsModel.setUp(messenger, channelModel)
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

    private fun getData(viewState: DetailsViewState): ChannelDetailsModelStateData {
        return ChannelDetailsModelStateData(
            state = viewState.toModelState(),
            isLoading = viewState is Loading,
            data = (viewState as? Loaded)?.snippet?.toModelData(),
        ).also {
            oldState = viewState
        }
    }

    private fun getEvent(DetailsEvent: DetailsEvent): ChannelDetailsModelEventData {
        return ChannelDetailsModelEventData(
            event = DetailsEvent.toModelEvent(),
            value = (DetailsEvent as? Saved)?.snippetId.toString(),
        ).also {
            oldEvent = DetailsEvent
        }
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