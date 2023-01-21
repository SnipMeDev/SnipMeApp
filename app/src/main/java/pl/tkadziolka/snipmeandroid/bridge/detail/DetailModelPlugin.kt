package pl.tkadziolka.snipmeandroid.bridge.detail

import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.inject
import pl.tkadziolka.snipmeandroid.bridge.Bridge
import pl.tkadziolka.snipmeandroid.bridge.ModelPlugin
import pl.tkadziolka.snipmeandroid.bridge.toModelData

class DetailModelPlugin : ModelPlugin<Bridge.DetailModelBridge>(), Bridge.DetailModelBridge {
    private val model: DetailModel by inject()
    private var oldEvent: DetailEvent? = null
    private var oldState: DetailViewState? = null

    override fun getState(): Bridge.DetailModelStateData = getData(model.state.value)

    override fun getEvent(): Bridge.DetailModelEventData = getEvent(model.event.value)

    override fun resetEvent() {
        model.event.value = Idle
    }

    override fun onSetup(messenger: BinaryMessenger, bridge: Bridge.DetailModelBridge?) {
        Bridge.DetailModelBridge.setup(messenger, bridge)
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

    private fun getData(viewState: DetailViewState): Bridge.DetailModelStateData {
        return Bridge.DetailModelStateData().apply {
            state = viewState.toModelState()
            is_loading = viewState is Loading
            data = (viewState as? Loaded)?.snippet?.toModelData()
            oldHash = oldState?.hashCode()?.toLong()
            newHash = viewState.hashCode().toLong()
        }.also {
            oldState = viewState
        }
    }

    private fun getEvent(detailEvent: DetailEvent): Bridge.DetailModelEventData {
        return Bridge.DetailModelEventData().apply {
            event = detailEvent.toModelEvent()
            value = (detailEvent as? Saved)?.snippetId
            oldHash = oldEvent?.hashCode()?.toLong()
            newHash = detailEvent.hashCode().toLong()
        }.also {
            oldEvent = detailEvent
        }
    }

    private fun DetailViewState.toModelState() =
        when (this) {
            Loading -> Bridge.ModelState.LOADING
            is Loaded -> Bridge.ModelState.LOADED
            else -> Bridge.ModelState.ERROR
        }

    private fun DetailEvent.toModelEvent() =
        when (this) {
            is Saved -> Bridge.DetailModelEvent.SAVED
            is Deleted -> Bridge.DetailModelEvent.DELETED
            else -> Bridge.DetailModelEvent.NONE
        }
}