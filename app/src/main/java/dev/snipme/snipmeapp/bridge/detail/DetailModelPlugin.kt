package dev.snipme.snipmeapp.bridge.detail

import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.inject
import dev.snipme.snipmeapp.bridge.ModelPlugin
import dev.snipme.snipmeapp.bridge.toModelData
import dev.snipme.snipmeapp.channel.DetailModelBridge as ChannelDetailModelBridge
import dev.snipme.snipmeapp.channel.ModelState as ChannelModelState
import dev.snipme.snipmeapp.channel.DetailModelStateData as ChannelDetailModelStateData
import dev.snipme.snipmeapp.channel.DetailModelEvent as ChannelDetailModelEvent
import dev.snipme.snipmeapp.channel.DetailModelEventData as ChannelDetailModelEventData

class DetailModelPlugin : ModelPlugin<ChannelDetailModelBridge>(), ChannelDetailModelBridge {
    private val model: DetailModel by inject()
    private var oldEvent: DetailEvent? = null
    private var oldState: DetailViewState? = null

    override fun getState(): ChannelDetailModelStateData = getData(model.state.value)

    override fun getEvent(): ChannelDetailModelEventData = getEvent(model.event.value)

    override fun resetEvent() {
        model.event.value = Idle
    }

    override fun onSetup(messenger: BinaryMessenger, bridge: ChannelDetailModelBridge?) {
        ChannelDetailModelBridge.setUp(messenger, bridge)
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

    private fun getData(viewState: DetailViewState): ChannelDetailModelStateData {
        return ChannelDetailModelStateData(
            state = viewState.toModelState(),
            isLoading = viewState is Loading,
            data = (viewState as? Loaded)?.snippet?.toModelData(),
            oldHash = oldState?.hashCode()?.toLong(),
            newHash = viewState.hashCode().toLong(),
        ).also {
            oldState = viewState
        }
    }

    private fun getEvent(detailEvent: DetailEvent): ChannelDetailModelEventData {
        return ChannelDetailModelEventData(
            event = detailEvent.toModelEvent(),
            value = (detailEvent as? Saved)?.snippetId.toString(),
            oldHash = oldEvent?.hashCode()?.toLong(),
            newHash = detailEvent.hashCode().toLong(),
        ).also {
            oldEvent = detailEvent
        }
    }

    private fun DetailViewState.toModelState() =
        when (this) {
            Loading -> ChannelModelState.LOADING
            is Loaded -> ChannelModelState.LOADED
            else -> ChannelModelState.ERROR
        }

    private fun DetailEvent.toModelEvent() =
        when (this) {
            is Saved -> ChannelDetailModelEvent.SAVED
            is Deleted -> ChannelDetailModelEvent.DELETED
            else -> ChannelDetailModelEvent.NONE
        }
}